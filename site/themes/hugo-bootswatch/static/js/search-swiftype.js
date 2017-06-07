$(document).ready(function () {


    
    var config = {
        //https://api.swiftype.com/api/v1/engines/gigaspaces-docs/search.json' -H 'Content-Type: application/json' -d '{"auth_token":"iypuoKUJYuWKHyJs2oFp","per_page":2,"page":1,"q":"mongodb"}
        apiURL:'https://api.swiftype.com/api/v1/public/engines/search.json',
        params: {
            "engine_key":"vskywTXhmRpTsNEQ9nux",
            "per_page":50,
            "page":1, 
            "fetch_fields": {"page":["highlight", "body", "url", "title"]}, 
            "search_fields":{"page":["title^5", "body", "highlight", "url"]}
        }
    }

    
    $('#go-search').click(function (e) {  
        e.preventDefault();      
        if ($("#q").val() != null && $("#q").val().trim() != "")
            search();
        return false;
    });

    function shouldIncludeInResults(currentSection, section) {
        if (currentSection.docSection == "XAP" && section.docSection == "XAP") {
            if (currentSection.version != section.version) 
                return false; 
            if (currentSection.lang != null && section.lang != null &&
                currentSection.lang != section.lang) return false;             
        }  
        return true; 
    }

    function displaySearch(settings, data) {
        var resultsDiv = $('#page-content');
        if (resultsDiv.length == 0) resultsDiv = $('#home-content'); 
        var results = data.records.page;
        if (results) {         
            //handle back / forward event 
            //currentContent = resultsDiv.html();
            resultsDiv.text('');
            $("#toc").hide();
            var paginationButtons = "<ul class=\"pager\">" +
                                    "<li rel=\"prev\" class=\"previous disabled\"><a href=\"#\">&larr; Previous</a></li>" +
                                    "<li rel=\"next\" class=\"next disabled\"><a href=\"#\">Next &rarr;</a></li></ul>"
            resultsDiv.append("<h1 style=\"margin-top: 0px;\">Search Results</h1>");
            resultsDiv.append(paginationButtons);
            
            // If results were returned, add them to a content div
            resultsDiv.append("<ul id=\"results-ul\" style=\"padding-left:0px;\"/>");
            var ul = $("#results-ul");
            currentSection = getSection(window.location.href);
            for (var i = 0; i < results.length; i++) {                
                var section = getSection(results[i].url);
                if (shouldIncludeInResults(currentSection, section)) { 
                    ul.append(new result(results[i], section.desc) + '');
                }
            }

            ul.hide().appendTo(resultsDiv).fadeIn('slow');            
            resultsDiv.append(paginationButtons);

            
            if (settings.params.page > 1) {                
                $("[rel='prev']").attr("class", "previous enabled");
                $("[rel='prev']").click(function () {
                    search({params:{ page:settings.params.page - 1}});
                });
            } else {
                $("[rel='prev']").attr("class", "previous disabled");
                $("[rel='prev']").bind('click', false);
            }

            if (data.info.page.total_result_count > (settings.params.page) * settings.params.per_page) {
                $("[rel='next']").attr("class", "next enabled");
                $("[rel='next']").click(function () {
                    search({params:{page:settings.params.page + 1}});
                });
            } else {
                $("[rel='next']").attr("class", "next disabled");
                $("[rel='next']").bind('click', false);
            }
        }
        else {
            // No results were found for this search.
            resultsDiv.empty();
            $('<p>', {html:"<div style=\"height:500px\"><h3 class=\"searchNoResult\">Oops, we couldn't find what you were looking for. Try rephrasing your search</h3></div>"}).hide().appendTo(resultsDiv).fadeIn();        
        }
    }

    function search(settings) {
        $("#search-icon").removeClass("fa-search").addClass("fa-spinner").addClass("fa-spin");

        var query = $('#q').val() || "";
        if (query.trim() == "") return; 

        // If no parameters are supplied to the function,
        // it takes its defaults from the config object above:
        settings = $.extend(true, {}, config, settings);

        settings.params.q = query; 
        
        
        try {            
            $.ajax({
                type:'GET',
                url:settings.apiURL,
                dataType:'jsonp',
                data: settings.params,
                success: function(data) {
                    displaySearch(settings, data);
                },
                error: function(e) {
                    console.log(e +"");
                }, 
                complete: function() {                    
                    $("#search-icon").removeClass("fa-spinner").removeClass("fa-spin").addClass("fa-search");
                }, 
        
            });
        }
        catch (e) {
            console.log(e + '');
        }
        
    }

    function result(r, section) {

        // This is class definition. Object of this class are created for
        // each result. The markup is generated by the .toString() method.

        
        this.re = /http:\/\/.*\/(.*)\/.*/;

        var title = r.highlight.title; 
        if (!title) title = r.title; 

        var snippet = r.highlight.body; 
        if (!snippet) snippet = r.body; 

        var arr = [
            '<li class="search-result">',
            '<h4><a href="',r.url,'">','[',section,'] ',title,'</a></h4>',
            '<p>',
            r.highlight.body,'&nbsp;&nbsp;',
            '<a href="',r.url, '">Read More &raquo;</a>',
            '</p>',            
            '</li>'
        ];

        // The toString method.
        this.toString = function () {
            return arr.join('');
        }
        
    }    

    function endsWith(str, suffix) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    }

    function startsWith(str, prefix) {
        return str.indexOf(prefix) == 0;
    }

	function getSection(url) {
        if (url) {
            var urlSections = url.split("/");
            if (urlSections.length > 3 && urlSections[3]) {
                var sectionPath = urlSections[3];
				switch (sectionPath) {
					case "sbp": 				return {desc: "Solutions &amp; Patterns", docSection: "SBP"};
					case "api_documentation":	return {desc: "API Documentation", docSection: "API"};
					case "early_access":		return {desc: "Early Access", docSection: "EA"};
					case "product_overview":	return {desc: "Product Overview", docSection: "PO"};
					case "tutorials":			return {desc: "Tutorials", docSection: "TUT"};
					case "howto":				return {desc: "How To", docSection: "HT"};
					case "release_notes":		return {desc: "Release Notes", docSection: "EA"};
					case "faq":					return {desc: "FAQ", docSection: "FAQ"};
					case "videos":				return {desc: "Videos", docSection: "VID"};
					case "xap97":				return {desc: "XAP 9.7",       version:"9.7", docSection:"XAP", lang:"java"};
					case "xap97net":			return {desc: "XAP.NET 9.7",   version:"9.7", docSection:"XAP", lang:"dotnet"};
					case "xap97adm":			return {desc: "XAP 9.7 Admin", version:"9.7", docSection:"XAP"};
					case "xap":		
						var version = urlSections[4];
						var section = urlSections[5];
						switch (section) {
							case "admin":
								return {desc: "XAP " + version + " Admin", version:version, docSection:"XAP"};;
							case "security":
								return {desc: "XAP " + version + " Security", version:version, docSection:"XAP"};;
							case "dev-dotnet":
							case "tut-dotnet":
								return {desc: "XAP.NET " + version, version:version, docSection:"XAP", lang:"dotnet"};
							default:
								return {desc: "XAP " + version, version:version, docSection:"XAP",lang:"java"};
						}
				}
				
                if (startsWith(sectionPath, "xap")) {
					if (endsWith(url,"xap121.html")) return {desc: "XAP 12.1", version:"12.1", docSection:"XAP"};
					if (endsWith(url,"xap120.html")) return {desc: "XAP 12.0", version:"12.0", docSection:"XAP"};
					if (endsWith(url,"xap110.html")) return {desc: "XAP 11.0", version:"11.0", docSection:"XAP"};
					if (endsWith(url,"xap102.html")) return {desc: "XAP 10.2", version:"10.2", docSection:"XAP"};
					if (endsWith(url,"xap101.html")) return {desc: "XAP 10.1", version:"10.1", docSection:"XAP"};
					if (endsWith(url,"xap100.html")) return {desc: "XAP 10.0", version:"10.0", docSection:"XAP"};
					if (endsWith(url,"xap97.html"))  return {desc: "XAP 9.7",  version:"9.7",  docSection:"XAP"};
                }
            }            
        }
        return {desc:"", docSection:""};
    }

    var customRenderFunction = function(document_type, item) {
        var title = item["highlight"]["title"];
        var section = getSection(item["url"]).desc;
    
        if (!title) {
            title = item["title"]
        }
        var out = "<p class='title'>[" + section + "] " + title + "</p>";
        if (item["highlight"]["sections"]) {
            return out.concat("<p class='sections'>" + item["highlight"]["sections"] + "</p>");
        }
        return out;
    }

    var myResultRenderFunction = function(ctx, results) {
        var $list = ctx.list, config = ctx.config;
        
        var currentSection = getSection(window.location.href);

        $.each(results, function(document_type, items) {
          $.each(items, function(idx, item) {
            if (shouldIncludeInResults(currentSection, getSection(item["url"]))) {
                ctx.registerResult($('<li>' + config.renderFunction(document_type, item) + '</li>').appendTo($list), item);
            }
          });
        });
      };

    $("#q").swiftype({
        renderFunction: customRenderFunction,
        resultRenderFunction: myResultRenderFunction, 
        engineKey: "vskywTXhmRpTsNEQ9nux"
    });

});


