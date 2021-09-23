<#macro ms_file jsonString>
    <#if jsonString??&&jsonString!=''>
        <@compress>
            ${jsonString?eval[0].path}
        </@compress>
    </#if>
</#macro>

<#macro ms_len text len>
    <#if text?length lt (len+1)?number>
        ${text}
    <#else>
        ${text[0..len?number]}...
    </#if>
</#macro>
