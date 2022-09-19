/* based on script from */
/* https://css-tricks.com/automatic-table-of-contents */
$(document).ready(function() {
		
	 
		//		console.log("Hello world!..bbb.");
	 
	 
		/*
		style='list-style-type:square  !important;'
		
	 
		/* check if h2 or h3 exists on page. Only count visible headings */
		
		//		console.log("11111");

		
		//		if (f($("#" + name).length == 0)  {
		if ($("#dfb_toc_here").length != 0) {
			//		var element_toc_marker = document.getElementById("dfb_toc");
			//
			//		if(typeof(element_toc_marker) != 'undefined' && element != null){
			
			//			$("#dfb_toc_block_none").insertAfter("#dfb_toc_here");
			$("#page_toc").insertAfter("#dfb_toc_here");
			
			//		console.log("22222");

         
			/* build the opening tags of the structure */
			var ToC =
				"<nav  style='list-style-type:none !important;'>" +
				"<ul  >";
			/*			var ToC =
			"<nav role='navigation' class='table-of-contents'>" +
			"<ul  style='list-style-type:none;' >";
			*/			
      
			/* set up variables used */
			var newLine, el, title, link, tag;
			var element_class;
			var skip_title;
			/* set linkCount to zero */
			var linkCount = 0;
			var H1Count = 0;
			
			//		console.log("33333");

			
         
			/* find all h2 and h3 --- and h4 --- and h1 ---- on page. Only count visible headings */
			$("h1:visible,h2:visible,h3:visible,h4:visible").each(function() {
         
					//		console.log("44444");
					/* get the heading */
					el = (this);
					//		console.log("el is " + el);
					//			console.log("55555");
					/* get the heading title */
					title = el.text();
					element_class = el.attr("class");
					if ((element_class == "tc-pagetitle") || (element_class == "tc-pagetitle2"))
					{skip_title = true}
					else
					{skip_title = false}
					//		 console.log("class >>>>" + element_class + "<<<<<"); 
					/* get the heading tag, this is capitalised, i.e. 'H2' or 'H3' or 'H1'*/
					tag = el.prop("tagName");
      
					/* updated linkCount, this will be id for link */
					linkCount += 1;
					if (tag == 'H1') {H1Count += 1;}
					linkCount += 1;

					link = "link" + linkCount;
					/* insert an anchor tag with the name attribute */
					$(this).prepend('<a name="' + link + '"></a>');

					/* Build the line in the list, setting the li class as the tag name, and using the heading text */

					
					/*					if ((tag == 'H1') && (H1Count != 1))  */
					//					if ((tag == 'H1') && (element_class != "tc-pagetitle") && (element_class != "tc-pagetitle2"))
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
						//	console.log("h2 has " + title);
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
					
						//	console.log(title);
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
						//	console.log("h4 has " + title);
					
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
					/*			if ((tag == 'H1') && (H1Count == 1))  */
					//					if ((element_class != "tc-pagetitle") && (element_class != "tc-pagetitle2"))
					if (!skip_title)
					{ToC += newLine;}

				});   
         
			/* Add closing tags to list */   
			ToC +=
				"</ul>" +
				"</nav>";
			
			
			
			/* Insert list in topic, and make visible */
			//			$("#page-toc").append(ToC).css("display", "block");
			$("#page-toc-body").append(ToC);
//			$("#page-toc-body").append(ToC).css("display", "none");
         
		}
   
	});