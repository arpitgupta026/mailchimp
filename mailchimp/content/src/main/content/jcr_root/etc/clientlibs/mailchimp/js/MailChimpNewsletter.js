function sendNewsLetterToMailChimp(ref){
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
                if(listID){
                    var params = {};
					params["campaignID"] = listID;
                    params["configs"] = configURL;
                    $.ajax({
                        url: '/services/mailchimp/campaigns?action=send',
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