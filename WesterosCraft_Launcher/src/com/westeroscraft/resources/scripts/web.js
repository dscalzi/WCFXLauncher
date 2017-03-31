/**
 * This script modifies the styles of several of the pages that
 * are loaded using the web browser on the launcher. These
 * improvements are only cosmetic to allow the web the select pages
 * to display seamlessly. Note that any changes done on the websites
 * modified may require updates to this script. The current modified
 * websites are:
 * 
 * http://westeroscraft.com (for display as of Jan. 5, 2017)
 * 
 * @author Daniel D. Scalzi
 * 
 */
$(document).ready(function() {
	//If the site currently loaded is WesterosCraft's website.
	//Allows flexibility for SSL and TLD changes.
	if(document.location.href.toLowerCase().indexOf("://www.westeroscraft.") > -1){
		const urlSplit = document.location.pathname.split("/");
		
		//Scroll positions for pages with the pathname given.
		var pageScrolls = new Map();
		pageScrolls.set("servernews", 145);
		pageScrolls.set("guide", 185);
		pageScrolls.set("map", 136);
		
		//Reduced spacings for pages with the pathname given.
		var spacerAdjusts = new Map();
		spacerAdjusts.set("servernews", "-65px");
		spacerAdjusts.set("guide", "-65px");
		spacerAdjusts.set("map", "-71px");
		
		//Make the navbar static if it exists.
		var header = document.getElementsByClassName("navbar navbar-inverse main_nav navbar-fixed-top")[0];
		if(header != null){
			header.className = "navbar navbar-inverse main_nav navbar-static-top";
		}
		
		//Reduce spacing for the page if specified.
		var cont = document.getElementsByClassName("fixed_height_elem container_spacer first")[0];
		if(cont != null && spacerAdjusts.get(urlSplit[1]) != null){
			cont.style.marginTop = spacerAdjusts.get(urlSplit[1]);
		}
		
		//Adjustments for the guide page.
		var guide = document.getElementsByClassName("guide_page")[0];
		var guidePanel = document.getElementsByClassName("toc_panel")[0];
		if(guide != null && guidePanel != null){
			guide.style.marginTop = "-65px";
			guidePanel.style.marginTop = "-65px";
		}
		
		//Scroll page to desired location if specified.
		if(pageScrolls.get(urlSplit[1]) != null){
			window.scrollTo(0,pageScrolls.get(urlSplit[1]));
		}
	}
	
	//Force vertical scrollbar to be always visible.
	if(document.body.scrollHeight < document.body.clientHeight){
		$('html').css('overflow-y', 'scroll');
	}
	
});