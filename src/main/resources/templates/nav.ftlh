<#import "/spring.ftl" as spring/>

<ul class="nav">

    <li class="nav-item">
        <a class="nav-link" href="#" style="font-size: 16px"><@spring.message "navigation.index" /></a>
    </li>
    <li class="nav-item">
        <a class="nav-link active" href="text2page"><@spring.message "navigation.text2page" /></a>
    </li>
    <li class="nav-item">
        <a class="nav-link" href="image2page"><@spring.message "navigation.image2page" /></a>
    </li>
    <li class="nav-item">
        <a class="nav-link" href="javascript:void(0)" data-bs-toggle="modal" data-bs-target="#settingModal"><@spring.message "navigation.settings" /></a>
    </li>
</ul>