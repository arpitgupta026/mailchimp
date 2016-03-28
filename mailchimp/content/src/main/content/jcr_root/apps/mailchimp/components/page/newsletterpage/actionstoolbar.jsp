
<%@page session="false" import="com.day.cq.i18n.I18n,
                com.day.cq.wcm.api.WCMMode,
                com.day.cq.wcm.api.components.Toolbar" %><%!
%><%@include file="/libs/foundation/global.jsp" %><%
    // draw the 'edit' bar explicitly
    if (editContext != null) {
%><table cellpadding="0" cellspacing="0" width="560" border="0"
         style="margin: 0 auto;"><tbody><tr>
    <td><div><%
        editContext.getComponentContext().setDecorate(false);
        editContext.getEditConfig().setOrderable(false);
        Toolbar tb = editContext.getEditConfig().getToolbar();
        tb.remove(0);
        tb.add(new Toolbar.Label(I18n.get(slingRequest, "Newsletter")));
        tb.add(new Toolbar.Separator());
        tb.add(new Toolbar.Button(I18n.get(slingRequest, "Settings"),
        		"function() { CQ.mcm.utils.Newsletter.openDialog(this,\"/apps/mailchimp/components/page/newsletterpage/dialog.infinity.json\"); }",
                false,
                I18n.get(slingRequest, "Edit the newsletter settings")));
        tb.add(new Toolbar.Separator());
        tb.add(new Toolbar.Button(I18n.get(slingRequest, "Test..."),
                "function() { CQ.mcm.utils.Newsletter.openDialog(this, \"/libs/mcm/components/newsletter/flighttest/dialog.infinity.json\"); }",
                false,
                I18n.get(slingRequest, "Test the newsletter")));
        tb.add(new Toolbar.Button(I18n.get(slingRequest, "Send..."),
                "function() { CQ.mcm.utils.Newsletter.openDialog(this, \"/apps/mailchimp/components/mcm/sendwizard/dialog.infinity.json\"); }",
                false,
                I18n.get(slingRequest, "Send the newsletter")));                      
        //required to have the editbar into the table.
        out.flush();
        editContext.includeEpilog(slingRequest, slingResponse, WCMMode.EDIT);
%></div></td></tr></tbody></table><%
    }
%>
