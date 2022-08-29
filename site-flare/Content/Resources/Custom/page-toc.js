/* based on script from */
/* https://css-tricks.com/automatic-table-of-contents */

 $(document).ready(function() {
	 
	 
	 console.log("Hello world!");
	 
	 
	 
	 
   /* check if h2 or h3 exists on page. Only count visible headings */
   if ($("h2:visible,h3:visible").length > 0) {
         
      /* build the opening tags of the structure */
      var ToC =
         "<nav role='navigation' class='table-of-contents'>" +
         "<ul>";
      
      /* set up variables used */
      var newLine, el, title, link, tag;
      /* set linkCount to zero */
      var linkCount = 0;
         
      /* find all h2 and h3 --- and h4 ---on page. Only count visible headings */
      $("h2:visible,h3:visible,h4:visible").each(function() {
         
         /* get the heading */
         el = $(this);
         /* get the heading title */
         title = el.text();
         /* get the heading tag, this is capitalised, i.e. 'H2' or 'H3' */
         tag = el.prop("tagName");
      
         /* updated linkCount, this will be id for link */
         linkCount += 1;

         link = "link" + linkCount;
         /* insert an anchor tag with the name attribute */
         $(this).prepend('<a name="' + link + '"></a>');

         /* Build the line in the list, setting the li class as the tag name, and using the heading text */
					
					if (tag == 'H2')
					{						
					
						newLine =
							"<li class=" +
							tag +
							">" +
							"<a href='#" + link + "'>" +
							title +
							"</a>" +
							"</li>";
					}
					
					if (tag == 'H3')
					{						
					
						newLine =
							"<ul  style='list-style-type:square;'   ><li class=" +
							tag +
							">" +
							"<a href='#" + link + "'>" +
							title +
							"</a>" +
							"</li></ul>";
					}
					
					if (tag == 'H4')
					{						
					
						newLine =
							"<ul> <ul><li class=" +
							tag +
							">" +
							"<a href='#" + link + "'>" +
							title +
							"</a>" +
							"</li></ul></ul>";
					}


         
         /* add the list item to the list */
         ToC += newLine;

      });   
         
      /* Add closing tags to list */   
      ToC +=
         "</ul>" +
         "</nav>";
      /* Insert list in topic, and make visible */
      $("#page-toc").append(ToC).css("display", "block");
         
   }
   
});