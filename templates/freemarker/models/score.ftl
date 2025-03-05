<div class="col col-12 <#if !full??>col-md-6</#if> act-entry <#if (index >= 4)> d-none</#if> d-flex flex-column mb-4">
    <#if isActivity??>
    <div class="row p-2 align-items-center">
        <div class="col-auto">
            <img class="flag" src="${avatarServer}/${score.id?c}" alt="">
        </div>
        <div class="col-auto d-flex align-items-center">
            <span class="h6">
                ${score.name}
            </span>
            <small class="ms-2" data-timestamp-format="date" data-timestamp="${score.playTime}">
                ${score.playTime}
            </small>
        </div>
    </div>
    </#if>

    <#assign name = score.mapFilename?replace(".osu", "")>
<#assign height=30>

<#assign sanitizedName = name?replace('.osu', '')>


<div class="score-card mb-3">
    <div class="score-container bg-dark shadow">
        <!-- Left side with image and gradient overlay -->
        <div class="beatmap-image-container">
            <div class="beatmap-image" style="background-image: url('/api/v1/thumb?setId=${score.set_id?c}')"></div>
            <div class="image-overlay"></div>
            <#include "/freemarker/gradeconvert.ftl">

        </div>
        
        <!-- Right side with details -->
        <div class="score-details text-light">
            <!-- Action buttons moved to top-right of details section with higher z-index -->
            <div class="score-actions">
                <a href="/scores/${score.id}" class="action-button view-button bg-dark bg-opacity-50 text-light" title="View Score">
                    <i class="fas fa-eye"></i>
                </a>
                <a href="/v1/get_replay?id=${score.id}" class="action-button download-button bg-dark bg-opacity-50 text-light" title="Download Replay">
                    <i class="fas fa-download"></i>
                </a>
            </div>
            
            <!-- Title with increased right padding to avoid buttons -->
            <div class="beatmap-title fw-medium">${sanitizedName}<span class="mod-pill badge bg-warning bg-opacity-25 text-warning border border-warning border-opacity-25">${score.mods?join(", ")}</span></div>
            
            <div class="score-stats">
                <div class="main-stats">
                    <div class="pp-display text-info fw-bold">${score.pp?string("0")}<span class="text-info-emphasis">pp</span></div>
                    <div class="acc-display text-light opacity-75">${score.acc?string("0.00")}<span>%</span></div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>