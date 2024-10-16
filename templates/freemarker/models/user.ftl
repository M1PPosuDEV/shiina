<div class="col col-12 <#if !u.isOwner??>col-md-6</#if> member-entry <#if (index >= 4)> d-none</#if>">
    <a href="/u/${u.id}" class="bg-secondary m-2 p-3 row leaderboard-panel" style="border-radius: 5px;">
        <span class="m-1 m-sm-0 col-auto d-flex align-items-center">
            <img class="flag" src="https://a.osunolimits.dev/${u.id}" alt="">
        </span>
        <span class="m-1 m-sm-0 col-auto d-none d-lg-flex align-items-center">
            <img class="flag" src="/img/flags/${u.country}.svg" alt="${u.country} Flag">
        </span>
        <#if u.isOwner??>
            <span class="m-1 m-sm-0 col-auto d-flex align-items-center">
                <i class="fa-solid fa-crown fa-2xl"></i>
            </span>
        </#if>
        <span class="m-1 m-sm-0 col-auto"
            style="font-size: calc((var(--bs-font-size-base) + .3000rem) + .5vw);"></span>
        <span class="m-1 m-sm-0 col-auto d-flex align-items-center">
            <span class="ms-2">
                ${u.name}
            </span>
        </span>
    </a>
</div>