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
CQ.mcm.Dashboard = CQ.Ext.extend(CQ.Ext.Panel, {

    constructor: function(config) {

        var db = this;

        var listsStoreConfig = CQ.Util.applyDefaults({}, {
            "autoLoad": true,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": "/libs/cq/security/content/authorizableSearch.json",
                "method": "GET"
            }),
            "reader": new CQ.Ext.data.JsonReader(
                {
                    "totalProperty": "results",
                    "root": "authorizables",
                    "id": "home"
                },
                CQ.Ext.data.Record.create([
                    {
                        "name": "name",
                        "mapping": CQ.shared.XSS.getXSSPropertyName("name")
                    },
                    {
                        "name": "home"
                    },
                    {
                        "name": "membersTotal"
                    }
                ])
            ),
            "baseParams": {
                "_charset_": "utf-8",
                "ml": 2000, // membersLimit
                "props": "name,home,membersTotal",
                "query": new CQ.security.search.Query(CQ.Util.applyDefaults(config.queryCfg, {
                    "category": "mcm",
                    "selector": "group",
                    "totalMax": 10,
                    "sortBy": "@cq:lastModified",
                    "sortDir": "desc"
                })).getString()
            }
        });

        var segmentsStoreConfig = CQ.Util.applyDefaults({}, {
            "autoLoad": true,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": "/bin/wcm/contentfinder/page/view.json/content",
                "method": "GET"
            }),
            "reader": new CQ.Ext.data.JsonReader(
                {
                    "root": "hits",
                    "id": "name"
                },
                CQ.Ext.data.Record.create([
                    {
                        "name": "title",
                        "mapping": CQ.shared.XSS.getXSSPropertyName("title")
                    },
                    {
                        "name": "lastModified"
                    },
                    {
                        "name": "path"
                    }
                ])
            ),
            "baseParams": {
                "_charset_": "utf-8",
                "query": "path:/etc/segmentation",
                "type": "cq:Page"
            }
        });

        var reportsStoreConfig = CQ.Util.applyDefaults({}, {
            "autoLoad": true,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": "/bin/wcm/contentfinder/page/view.json/content",
                "method": "GET"
            }),
            "reader": new CQ.Ext.data.JsonReader(
                {
                    "root": "hits",
                    "id": "name"
                },
                CQ.Ext.data.Record.create([
                    {
                        "name": "title",
                        "mapping": CQ.shared.XSS.getXSSPropertyName("title")
                    },
                    {
                        "name": "lastModified"
                    },
                    {
                        "name": "path"
                    }
                ])
            ),
            "baseParams": {
                "_charset_": "utf-8",
                "query": "path:/etc/reports",
                "type": "cq:Page"
            }
        });

        var campaignsStoreConfig = CQ.Util.applyDefaults({}, {
            "autoLoad": true,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": "/bin/wcm/contentfinder/page/view.json/content",
                "method": "GET"
            }),
            "reader": new CQ.Ext.data.JsonReader(
                {
                    "root": "hits",
                    "id": "name"
                },
                CQ.Ext.data.Record.create([
                    {
                        "name": "title",
                        "mapping": CQ.shared.XSS.getXSSPropertyName("title")
                    },
                    {
                        "name": "lastModified"
                    },
                    {
                        "name": "path"
                    }
                ])
            ),
            "baseParams": {
                "_charset_": "utf-8",
                "query": "path:/content/campaigns",
                "type": "cq:Page"
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
                                        "text": CQ.I18n.getMessage("New List.dddd.."),
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
                                                    '<span class="cq-security-grid-link" onclick="CQ.security.SecurityAdmin.showGroupInGrid(\'{home}\');">{name}</span>' +
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
                    }                 
                ]
            }
        });

        CQ.mcm.Dashboard.superclass.constructor.call(this, config);
    }

});

CQ.Ext.reg("mailchimpDashboard", CQ.mcm.Dashboard);
