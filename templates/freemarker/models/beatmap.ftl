<div class="col-12  col-lg-<#if bigBm??>12 h-100<#else>6</#if> flex-column">

    <div class="score-card mb-3 h-100">
        <div class="score-container bg-dark shadow  h-100">
            <!-- Left side with image and gradient overlay -->
            <div class="beatmap-image-container">
                <div class="beatmap-image" style="background-image: url('/api/v1/thumb?setId=${beatmap.set_id?c}')">
                </div>
                <div class="image-overlay w-100 h-100 d-flex align-items-end justify-content-start p-2">
                    <span class="badge fs-6 bg-${convertStatusBackColor(beatmap.status)}">
                        ${convertStatusBack(beatmap.status)}
                    </span>
                </div>
            </div>

            <!-- Right side with details -->
            <div class="score-details text-light">
                <!-- Action buttons moved to top-right of details section with higher z-index -->
                <div class="score-actions">
                    <a href="/b/${beatmap.id?c}" class="action-button view-button bg-dark bg-opacity-50 text-light"
                        title="View Score">
                        <i class="fas fa-eye"></i>
                    </a>
                    <a href="https://osu.direct/api/d/${beatmap.set_id?c}"
                        class="action-button download-button bg-dark bg-opacity-50 text-light" title="Download Replay">
                        <i class="fas fa-download"></i>
                    </a>
                </div>

                <!-- Title with increased right padding to avoid buttons -->
                <div class="beatmap-title fw-medium">${beatmap.filename?replace(".osu", "")}</div>
                <div class="artist-title">
                    mapped by ${beatmap.creator}
                </div>
                <div class="score-stats mt-3">
                    <div class="main-stats">
                        <i class="fa-solid fa-star"></i>
                        ${beatmap.diff?string("0.00")}
                        <i class="fa-solid fa-play"></i>
                        ${beatmap.plays}
                        <i class="fa-solid fa-circle-check"></i>
                        ${beatmap.passes}
                        <i class="fa-solid fa-calendar"></i>
                        <#if beatmap.last_update??>
                            <span data-timestamp-format="date" data-timestamp="${beatmap.last_update}">
                                ${beatmap.last_update}
                            </span>
                        </#if>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>