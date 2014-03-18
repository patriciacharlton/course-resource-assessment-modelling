$(document).ready(function()
{
	showInfoWindow = function(e)
	{
		e.preventDefault();
		var content = $("#troubleshooting").clone();
		content.addClass("winContent");
		content.attr("title", "Launching the CRAM Tool");
		$(content).dialog({
	      resizable: false,
	      height: 500,
	      width: 600,
	      modal: true,
	      buttons: {
	        "Launch": function() {
	          //form.submit();
				if (!deployJava.isWebStartInstalled("undefined")) 
				{
					if (deployJava.installLatestJRE()) 
					{
						if (deployJava.launch("launch.jnlp")) {
							$(this).dialog("destroy");
						}
					}
				} else {
					if (deployJava.launch("launch.jnlp")) {
						$(this).dialog("destroy");
					}
				}
	        },
	        "Get MacOSX version": function()
	        {
	        	window.location="CRAM%20Tool.zip";
	        	$(this).dialog("destroy");
	        },
	        "Get Windows version": function()
	        {
	        	window.location="CRAM%20Tool.exe";
	        	$(this).dialog("destroy");
	        }	        
	      }
	    });
	};

	$("#launchWindow").on("click", showInfoWindow);
});

