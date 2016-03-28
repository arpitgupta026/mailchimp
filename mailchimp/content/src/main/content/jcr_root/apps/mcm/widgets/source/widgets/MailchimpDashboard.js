/*
 * Copyright 1997-2010 Day Management AG
 * Barfuesserplatz 6, 4001 Basel, Switzerland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Day Management AG, ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Day.
 */

/**
 * @class CQ.mcm.Dashboard
 * @extends CQ.Ext.Panel
 * The dashboard of the Marketing Campaigns Managment console.
 * @constructor
 * Creates a new Dashboard.
 * @param {Object} config The config object
 * @since 5.4
 */
CQ.mcm.MailchimpDashboard = CQ.Ext.extend(CQ.Ext.Panel, {

    constructor: function(config) {

        var db = this;

        var listsStoreConfig = CQ.Util.applyDefaults({}, {
            "autoLoad": true,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": "/services/mailchimp/import/lists",
                "method": "GET"
            }),
            "reader": new CQ.Ext.data.JsonReader(
                {
                    "totalProperty": "total_items",
                    "root": "lists",
                    "id": "id"
                },
                CQ.Ext.data.Record.create([
                    {
                        "name": "name"
                    },
                    {
                        "name":"id"
                    },
                    {
                        "name": "membersTotal",
                        "mapping": "stats.member_count"
                    }
                ])
            )
        });


        var campaignsStoreConfig = CQ.Util.applyDefaults({}, {
            "autoLoad": true,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": "/services/mailchimp/campaigns",
                "method": "GET"
            }),
            "reader": new CQ.Ext.data.JsonReader(
                {
					"totalProperty": "total_items",
                    "root": "campaigns",
                    "id": "id"


                },
                CQ.Ext.data.Record.create([
                    {
                        "name": "title",
                        "mapping":"settings.title"
                    },
                    {
                        "name": "id"
                    },
                    {
                        "name": "create_time"
                    }

                ])
            ),
            "baseParams": {
                "action": "fetchAll"
            }    
        });

        CQ.Util.applyDefaults(config,  {
            "xtype": "panel",
            "layout": "border",
            "border": false,
            "items": {
                "xtype": "panel",
                "region": "center",
                "margins": "5 5 5 5",
                "padding": "26px 16px 16px 16px",
                "border": true,
                "autoScroll": true,
                "cls": "cq-security-dashboard",
                "items": [
                    {
                        "xtype": "panel",
                        "layout": "hbox",
                        "autoHeight": true,
                        "border": false,
                        "items": [
                            // @deprecated: please use Adobe Campaign instead, as of AEM 6.1 (CQ-33156)
                            this.listsPanel = new CQ.Ext.Panel({
//                                "title": "Lists",
                                "flex": 1,
                                "height": 240,
                                "autoScroll": true,
                                "margins": "0 20 20 0",
                                "tbar": [
                                   CQ.I18n.getMessage("Mailchimp Lists"),
                                    "->",
                                        "-",
                                    {
                                        "text": CQ.I18n.getMessage("New List..."),
                                        "handler": function() {
                                            CQ.security.SecurityAdmin.createGroup();
                                        }
                                    },
                                    {
                                        "text": CQ.I18n.getMessage("Import Leads..."),
                                        "handler": function() {
                                            CQ.mcm.MCMAdmin.importCSV();
                                        }
                                    },
                                    "-",
                                    {
                                        "iconCls": "x-tbar-loading",
                                        "handler": function() {db.listsDataView.getStore().reload();}
                                    }
                                ],
                                "items": [
                                    this.listsDataView = new CQ.Ext.DataView({
                                        "cls": "cq-security-dashboard-dataview",
                                        "title": "Lists",
                                        "autoHeight": true,
//                                        "loadingText": CQ.I18n.getMessage("Loading content..."),
                                        "multiSelect": false,
                                        "singleSelect": true,
                                        "overClass": "x-view-over",
                                        "emptyText": CQ.I18n.getMessage("No items to display"),
                                        "tpl":
                                            '<table border="0" width="100%"><tbody>' +
                                            '<tpl for=".">' +
                                                '<tr class="row"><td>' +
                                                    '<span class="cq-security-grid-link" onclick="CQ.security.SecurityAdmin.showGroupInGrid(\'{id}\');">{name}</span>' +
                                                '</td><td class="cq-security-dashboard-right">' +
                                                    '{membersTotal}' +
                                                '</td></tr>' +
                                            '</tpl>' +
                                            '</tbody></table>',
                                        "itemSelector": ".row",
                                        "store": new CQ.Ext.data.GroupingStore(listsStoreConfig),
                                        "prepareData": function(data) {
                                            var max = db.listsDataView.getStore().baseParams.ml;
                                            if (data.membersTotal == max) {
                                                data.membersTotal = CQ.security.SecurityAdmin.formatMax(max);
                                            }
                                            return data;
                                        }
                                    })
                                ]
                            })
                        ]
                    },
                    {
                        "xtype": "panel",
                        "layout": "hbox",
                        "border": false,
                        "flex": 1,
                        "items": [
								this.campaignsPanel = new CQ.Ext.Panel({
//                                "title": "Campaigns",
                                "flex": 1,
                                "height": 240,
                                "autoScroll": true,
                                "tbar": [
                                    "Campaigns",//CQ.I18n.getMessage("Campaigns"),
                                    "->",
                                    {
                                    "iconCls": "x-tbar-loading",
                                    "handler": function() {db.campaignsDataView.getStore().reload();}
                                }],
                                "items": [
                                    this.campaignsDataView = new CQ.Ext.DataView({
                                        "cls": "cq-security-dashboard-dataview",
                                        "autoHeight": true,
//                                        "loadingText": CQ.I18n.getMessage("Loading content..."),
                                        "multiSelect": false,
                                        "singleSelect": true,
                                        "overClass": "x-view-over",
                                        "emptyText": CQ.I18n.getMessage("No items to display"),
                                        "tpl":
                                            '<table border="0" width="100%"><tbody>' +
                                            '<tpl for=".">' +
                                                '<tr class="row"><td>' +
                                                    '<span class="cq-security-grid-link" onclick="CQ.security.SecurityAdmin.showGroupInGrid(\'{id}\');">{title}</span>' +
                                                '</td><td class="cq-security-dashboard-right">' +
                                                    '{create_time}' +
                                                '</td></tr>' +
                                            '</tpl>' +
                                            '</tbody></table>',
                                        "itemSelector": ".row",
                                        "store": new CQ.Ext.data.GroupingStore(campaignsStoreConfig)

                                    })
                                ]
                            })
                        ]

                    }                 
                ]
            }
        });

        CQ.mcm.MailchimpDashboard.superclass.constructor.call(this, config);
    }

});

CQ.Ext.reg("mailchimpDashboard", CQ.mcm.MailchimpDashboard);
