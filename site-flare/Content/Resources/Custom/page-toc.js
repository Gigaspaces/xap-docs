/* based on script from */
/* https://css-tricks.com/automatic-table-of-contents */

$(document).ready(function() {
	 
		if ($("#NO-TOC").length != 0) 
		// if there is an id (span) of  "NO-TOC" then hide  TOC, else proceed to customize TOC
		{document.getElementById('page_toc').style.display = 'none';}
		else
		{
//		if (true) {
				// 1			$("#page_toc").insertAfter("#dfb_toc_here");
			
			/* build the opening tags of the structure */
			var ToC =
				"<nav  style='list-style-type:none !important;'>" +
				"<ul  >";
			var newLine, el, title, link, tag;
			var element_class;
			var skip_title;
			/* set linkCount to zero */
			var linkCount = 0;
			var H1Count = 0;
			var first_h1;
			var is_there_a_tc_pagetitle = false;
			
//			alert($(this).text());
//			alert($(this).attr("class"));
			
			
			/* find all h2 and h3 --- and h4 --- and h1 ---- on page. Only count visible headings */
			$("h1.tc-pagetitle:visible,h1:visible,h2:visible,h3:visible,h4:visible").each(function() {
         
					el = $(this);
					title = el.text();
					element_class = el.attr("class");
				//	alert(element_class);
					if (element_class == "tc-pagetitle") {is_there_a_tc_pagetitle = true;
				//		alert(title);
                        $("#page_toc").insertAfter($(this));
					}
					
				//	alert(element_class);
					
					
					if ((element_class == "tc-pagetitle") || (element_class == "tc-pagetitle2") || (element_class == "skip-toc"))
						{skip_title = true}
					else
			        	{skip_title = false}
					
					
					/* get the heading tag, this is capitalised, i.e. 'H2' or 'H3' or 'H1'*/
					tag = el.prop("tagName");
      
					/* updated linkCount, this will be id for link */
					linkCount += 1;
					if (tag == 'H1') {H1Count += 1;}
					if (H1Count == 1) {first_h1 = el;}
					linkCount += 1;

					link = "link" + linkCount;
					/* insert an anchor tag with the name attribute */
					$(this).prepend('<a name="' + link + '"></a>');

					/* Build the line in the list, setting the li class as the tag name, and using the heading text */

					
					if ((tag == 'H1') && (!skip_title))
					{						

						newLine =
							"<li class=" +
							tag +
							">" +
							"<a style='font-size: 120%;font-weight: 400;' href='#" + link + "'>" +
							title +
							"</a>" +
							"</li>";
					}
					
					
					if (tag == 'H2')
					{						
						newLine =
							"<ul   ><li class=" +
							tag +
							">" +
							"<a href='#" + link + "'>" +
							title +
							"</a>" +
							"</li></ul>";
					}
					
					if (tag == 'H3')
					{						
					
						newLine =
							"<ul  style='list-style-type:square;'   ><ul><li class=" +
							tag +
							">" +
							"<a href='#" + link + "'>" +
							title +
							"</a>" +
							"</li></ul></ul>";
					}
					
					if (tag == 'H4')
					{						
					
						newLine =
							"<ul> <ul><ul><li class=" +
							tag +
							">" +
							"<a href='#" + link + "'>" +
							title +
							"</a>" +
							"</li></ul></ul></ul>";
					}

					/* add the list item to the list */
					if (!skip_title)
					{ToC += newLine;}

					});   
         
			/* Add closing tags to list */   
			ToC +=
				"</ul>" +
				"</nav>";
			
			/* Insert list in topic, and make visible */
			$("#page-toc-body").append(ToC).css("display", "none");
			
// in a few cases, move TOC to a different place on the page:
			if ($("#dfb_force-toc_here").length != 0) {
				$("#page_toc").insertAfter("#dfb_force-toc_here");
			}	
			// alert('first-h1 is ' + first_h1);
			
			if (is_there_a_tc_pagetitle == false) {$("#page_toc").insertAfter(first_h1);}
			

			
         
		}
   
	});