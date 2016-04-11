function performAction(ref, action){
    if(ref && ref.path){
        var dlgPath = ref.path.replace("/jcr:content","");
        var url = CQ.HTTP.noCaching(dlgPath + ".cloudservices.json");
        var response = CQ.HTTP.get(url);
        if(response){
            var inheritData = CQ.HTTP.eval(response); 
            if(inheritData){
                var listID = inheritData["default-list"];
                var configs = inheritData["cq:cloudserviceconfigs"];
				var configURL = "";
                if(configs){
                    if($.isArray(configs)){
						configURL = configs.join(",");
                    }
                    else{
						configURL = configs;
                    }
                }
                if(listID && action && action.length > 0){
                    var params = {};
					params["listID"] = listID;
                    params["configs"] = configURL;
                    params["pagePath"] = ref.path;
                    $.ajax({
                        url: '/services/mailchimp/campaigns?action=' + action,
                        type: 'POST',
                        data: params,
                        dataType: "json",
                        success: function(data){
                        	if(data && data.title){

                                alert("Newsletter not sent");
                            }else{
								alert("Newsletter successfully sent");
                            }
						},
                        error:function(data){
                        	alert("Newsletter successfuly sent");
                    	}
                	});
                }
            }
        }    
    }
}