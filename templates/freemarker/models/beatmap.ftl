<div class="col-12 col-lg-<#if bigBm??>12 h-100<#else>6</#if> flex-column">
    <div class="card beatmap-card text-white bg-secondary h-100">
        <div class="row g-0 h-100">
            <div class="col-md-4 d-flex flex-grow-1">
                <div class="image-placeholder">
                    <div class="spinner-grow" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
                <img src="https://assets.ppy.sh/beatmaps/${beatmap.set_id?c}/covers/card.jpg"
                    class="img-fluid rounded-start card-img" alt="Beatmap Image"
                    onload="loadLazyLoadImage(this);"
                    onerror="lazyLoadNoImage(this, '${noBeatmapIcon}');">
            </div>
            <div class="col-md-8">
                <div class="card-body d-flex flex-column justify-content-between h-100">
                    <div>
                        <div class="card-title-bm">
                            <span class="<#if !smallText??>fs-5</#if>">
                                ${beatmap.filename?replace(".osu", "")}
                            </span>
                        </div>
                        <div class="card-artist">
                            <#if !smallText??>
                            <span class="card-text fw-light">by ${beatmap.artist}
                            </span>
                            <#else>
                            <small class="text-muted">by ${beatmap.artist} - mapped by ${beatmap.creator}</small>
                            </#if>
                        </div>
                         <#if !smallText??>
                        <div class="card-creator">
                            <small class="card-text">mapped by ${beatmap.creator}
                            </small>
                        </div>
                        </#if>
                        <div class="my-2">
                             <#if !smallText??><span class="text-muted"><#else><small class="text-muted"></#if>
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
                                <span class="badge bg-${convertStatusBackColor(beatmap.status)}">
                                    ${convertStatusBack(beatmap.status)}
                                </span>
       
                            <#if !smallText??></span><#else></small></#if>
                        </div>
                    </div>
                    <div class="hover-icons mt-2">
                        <a href="/b/${beatmap.id?c}<#if score??>?mode=${score.mode}</#if>" class="icon-link">
                            <i data-bs-toggle="tooltip" data-bs-placement="top" title='View Beatmap'  class="fas fa-eye"></i>
                        </a>
                        <a href="https://osu.direct/api/d/${beatmap.set_id?c}" class="icon-link">
                            <i data-bs-toggle="tooltip" data-bs-placement="top" title='Download'  class="fa-solid fa-download"></i>
                        </a>
                        <a href="osu://dl/${beatmap.set_id?c}" class="icon-link">
                            <i data-bs-toggle="tooltip" data-bs-placement="top" title='Download with osu!Direct' class="fa-solid fa-play-circle"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>