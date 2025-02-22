package dev.osunolimits.modules;

import java.sql.ResultSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import dev.osunolimits.common.Database;
import dev.osunolimits.common.MySQL;
import dev.osunolimits.plugins.events.admin.OnMultiAccountDetectionEvent;

public class ShiinaMultiDetection {
    private final Logger log = (Logger) LoggerFactory.getLogger("ShiinaMultiDetection");
    private final ScheduledExecutorService scheduler;

    private final String MULTI_SQL = "WITH UserPairs AS (SELECT c1.userid AS user1, c2.userid AS user2, c1.uninstall_id, c1.disk_serial, c1.adapters, c1.latest_time FROM client_hashes c1 JOIN client_hashes c2 ON c1.uninstall_id = c2.uninstall_id AND c1.userid != c2.userid) SELECT up.user1, up.user2, u1.name AS user1_name, u2.name AS user2_name, CASE WHEN MAX(uninstall_id) = MIN(uninstall_id) THEN 1 ELSE 0 END AS same_uninstall_id, CASE WHEN MAX(disk_serial) = MIN(disk_serial) THEN 1 ELSE 0 END AS same_disk_serial, CASE WHEN MAX(adapters) = MIN(adapters) THEN 1 ELSE 0 END AS same_adapters, MAX(latest_time) AS latest_time FROM UserPairs up JOIN users u1 ON up.user1 = u1.id JOIN users u2 ON up.user2 = u2.id GROUP BY up.user1, up.user2, u1.name, u2.name HAVING NOT (same_uninstall_id = 0 AND same_disk_serial = 0 AND same_adapters = 0) ORDER BY latest_time DESC;";
    
    public ShiinaMultiDetection() {
        ThreadFactory threadFactory = r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName("ShiinaMultiDetection");
            return thread;
        };
        
        this.scheduler = Executors.newSingleThreadScheduledExecutor(threadFactory);
        startScheduledTask();
        runHourlyTask();
    }
    
    private void startScheduledTask() {
        scheduler.scheduleAtFixedRate(this::runHourlyTask, 0, 1, TimeUnit.HOURS);
    }
    
    private void runHourlyTask() {
        MySQL mysql = Database.getConnection();

        try {   
            ResultSet multiAccountResultSet = mysql.Query(MULTI_SQL);
            while (multiAccountResultSet.next()) {
                int user1 = multiAccountResultSet.getInt("user1");
                int user2 = multiAccountResultSet.getInt("user2");
                boolean sameUninstallId = multiAccountResultSet.getBoolean("same_uninstall_id");
                boolean sameDiskSerial = multiAccountResultSet.getBoolean("same_disk_serial");
                boolean sameAdapters = multiAccountResultSet.getBoolean("same_adapters");
                
                int level = 0;
                if(sameDiskSerial) level++;
                if(sameUninstallId) level++;
                if(sameAdapters) level++;

                int affectedRows = mysql.Exec("INSERT INTO `sh_detections`(`user`, `target`, `detection`, `score`) VALUES (?,?,CURRENT_TIMESTAMP(),?)", user1, user2, level);
                if(affectedRows != -1) {
                    new OnMultiAccountDetectionEvent(user1, user2, level).callListeners();
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while running the hourly task", e);
        }finally {
            mysql.close();
        }
    }
    
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}