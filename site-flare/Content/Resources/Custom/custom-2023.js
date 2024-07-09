$(document).ready(function(){	
  $('.header-left').insertAfter('.logo-wrapper');
  $('.header-right').insertAfter('.header-left');
  if($('.product-bar').length) {
    $('.product-bar').insertAfter('#last-updated');
  }
  if($('.gs-banner').length) {
    $('.gs-banner').insertBefore('#last-updated');
  }
  if ($("#Glossary").length) {
    var glossaryAlphabetDiv = $('<div class="glossary-alphabet"></div>');
    
    var alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var bookmarkedLetters = [];
  
    $('.GlossaryPageHeading_0').each(function() {
      var headingText = $(this).text().trim();
      var headingId = 'alphabet-' + headingText;
      $(this).attr('id', headingId);
      bookmarkedLetters.push(headingText.toUpperCase());
    });
    for (var i = 0; i < alphabet.length; i++) {
      var letter = alphabet[i];
      var uppercaseLetter = letter.toUpperCase();
      var bookmarked = bookmarkedLetters.includes(uppercaseLetter);
      if (i > 0) {
        glossaryAlphabetDiv.append('<span class="pipe"> | </span>');
      }
      if (bookmarked) {
        var hyperlink = '<a href="#alphabet-' + uppercaseLetter + '">' + uppercaseLetter + '</a>';
        glossaryAlphabetDiv.append(hyperlink);
      } else {
        glossaryAlphabetDiv.append('<span>' + uppercaseLetter + '</span>');
      }
    }
    glossaryAlphabetDiv.insertAfter('h1#Glossary');
}
  $('html').addClass('show');
});

(function (w) {
  var _self = {
    props: {
      version: '2.0.0',
      prefix: 'TOPNAV:::',
      debug: location.protocol == 'file:' || location.hostname == 'localhost',
      domain: "\.gigaspaces\.com",
      isIframe: window.self !== window.top,
      prodVer: (new RegExp(/\/(\d+?[\.\d]*?|latest)\//).test(location.href)) ? location.href.match(/\/(\d+?[\.\d]*?|latest)\//)[1] : null,
    },
    log: function () {
      if (!_self.props.debug) return;
      var args = Array.prototype.slice.call(arguments);
      args.unshift(_self.props.prefix);
      console.log.apply(console, args);
    },
    version: function () {
      _self.log('version::', _self.props.version);
    },
    init: function () {
      var re = new RegExp(_self.props.domain + '$');
      if (re.test(location.hostname)) _self.props.debug = false;
      _self.ready();
    },
    ready: function () {
      $(function () {
        debug('Calling methods');
        _self.methods.breadcrumb();
        _self.methods.generateNavItems();
        _self.methods.addClipboardButtons();
        _self.methods.handleRightMenu();
        _self.methods.selectMenuItems();
        $('.sidenav').on('loaded', function () {
        $('#footer').addClass('show');
        });
        $("#sidenav-toggle-closed").prependTo(".sidenav-container")
        $("#sidenav-toggle-open").prependTo(".sidenav-container")
        $("#ipn-toggle-closed").prependTo(".ipn-content")
        $("#ipn-toggle-open").prependTo(".ipn-content")
        $("#sidenav-toggle-closed").css("visibility", "visible")
        $("#sidenav-toggle-open").css("visibility", "visible")
        $("#ipn-toggle-closed").css("visibility", "visible")
        $("#ipn-toggle-open").css("visibility", "visible")
        $("#sidenav-toggle-closed").click(function() {
            $(this).hide()
            $("#sidenav-toggle-open").show()
            $(".sidenav-wrapper").removeClass("closed")
            $(".sidenav-container ul.sidenav").removeClass("closed")
        }),
        $("#sidenav-toggle-open").click(function() {
            $(this).hide()
            $("#sidenav-toggle-closed").show()
            $(".sidenav-wrapper").addClass("closed")
            $(".sidenav-container ul.sidenav").addClass("closed")
        });
        $("#ipn-toggle-closed").click(function() {
          $(this).hide()
          $("#ipn-toggle-open").show()
          $(".ipn-content").removeClass("closed")
          $(".ipn-content ul.menu").removeClass("closed")
      }),
      $("#ipn-toggle-open").click(function() {
          $(this).hide()
          $("#ipn-toggle-closed").show()
          $(".ipn-content").addClass("closed")
          $(".ipn-content ul.menu").addClass("closed")
      });
      });
    },
    methods: {
      breadcrumb: function () {
          var homelink = $('a.logo').attr('href');
          console.log(homelink);
          if($('.breadcrumbs').length) {
            var breadcrumbVersion = $('#breadcrumb-version-number').text();
            $('.breadcrumbs').prepend('<span id="breadcrumb-home"><a class="MCBreadcrumbsLink" href="https://docs.gigaspaces.com/">Docs Home</a></span><span class="MCBreadcrumbsDivider"> &gt;&gt; </span><span id="breadcrumb-version"><a class="MCBreadcrumbsLink" href="'+ homelink +'">' + breadcrumbVersion + '</a></span><span class="MCBreadcrumbsDivider"> &gt;&gt; </span>')
          }
          $('a.logo').attr('href', 'https://www.gigaspaces.com/').attr('target','_blank');
      },
      addClipboardButtons: function() {
        var clipboardButton = '<div class="clipboard"><button title="Copy to clipboard"></button></div>';
        $('pre').prepend(clipboardButton);
        $('.clipboard').click( function() {
          var clipboardText = $(this).next('code');
          var clipboardRange = document.createRange();
          clipboardRange.selectNodeContents(clipboardText[0]);
          var selectedText = window.getSelection();
          selectedText.removeAllRanges();
          selectedText.addRange(clipboardRange);
          document.execCommand('copy');
          selectedText.removeRange(clipboardRange);
          var selectedButton = $(this).children('button');
          selectedButton.addClass('success');
          setTimeout(function(){
            $(selectedButton).removeClass('success');
          }, 2000);
          return false;
        });
      },

      generateNavItems: function () {
        _self.methods.buildNavItems();
      },
      buildNavItems: function () {
        var versionData = {
          '17.0': { 'url': '/17.0/landing.html', 'label': '17.0', 'hide': true },
          'latest': { 'url': '/latest/landing.html', 'label': '17.0' },
          '16.4': { 'url': '/16.4/landing.html', 'label': '16.4', 'topicBanner': 'old' },
          '16.3': { 'url': '/16.3/landing.html', 'label': '16.3', 'topicBanner': 'old' },
          '16.2.1': { 'url': '/16.2.1/landing.html', 'label': '16.2.1', 'topicBanner': 'old' },
          '16.2': { 'url': '/16.2/landing.html', 'label': '16.2', 'topicBanner': 'old' },
          '16.1.1': { 'url': '/16.1.1/landing.html', 'label': '16.1.1', 'topicBanner': 'old' },
          '16.1': { 'url': '/16.1/landing.html', 'label': '16.1', 'topicBanner': 'old' },
          '15.8': { 'url': '/15.8/landing.html', 'label': '15.8', 'topicBanner': 'old' },
          'Archive': { 'url': '/latest/admin/archive.html' }
        };

        var resourcesData = {
          'Resources': { 'url': '#' },
          'Solution Hub': { 'url': '/solution-hub/intro.html' },
          'Blog': { 'url': 'https://www.gigaspaces.com/blog', 'target': '_blank' }
        };

        var buttonsData = [
          '<button type="button" onclick="location.href=\'https://www.gigaspaces.com/downloads\'" class="navbar-btn btn-download">Download</button>'
        ];

        var topicBanner = {
          old: '<div class="tc-topic-banner old"><p>This page describes an older version of the product. The latest stable version is <a href="/latest/rn/latest.html">17.0</a>.</p></div>',
          preview: '<div class="tc-topic-banner preview"><p>This page describes the beta version of the product. The latest stable version is <a href="/latest/rn/latest.html">17.0</a>.</p></div>'
        };
      
        var vMenu = null;
        var mLabel, mTarget;
        var navBar = $('<div class="nav-extn-wrapper"></div>');
        var menuStart = '<ul id="MENU_ID" class="nav-menu"><li><a class="caret" href="#">MENU_LABEL</a><ul>';
        var menuEnd = '</ul></li></ul>';

        $(document)
          .mouseup(function (e) {
            if (!vMenu.is(e.target) && vMenu.has(e.target).length === 0) {
              $('.nav-menu ul').hide();
            }
          });

        for (var vKey in versionData) {
          var item = versionData[vKey];
          if (item.hide) continue;
          if (item.topicBanner && typeof topicBanner[item.topicBanner] !== 'undefined') {
            $('.top-container').prepend(topicBanner[item.topicBanner]);
          }
          mLabel = '<li><a href="' + item.url + '">' + item.label + '</a></li>';
          vMenu = $('#version-menu ul');
          if (!vMenu.length) {
            vMenu = $(menuStart.replace('MENU_LABEL', 'Versions').replace('MENU_ID', 'version-menu') + menuEnd).appendTo(navBar);
          }
          vMenu.append(mLabel);
        }

        for (var rKey in resourcesData) {
          var resource = resourcesData[rKey];
          mTarget = (resource.target) ? ' target="' + resource.target + '"' : '';
          mLabel = '<li><a href="' + resource.url + '"' + mTarget + '>' + rKey + '</a></li>';
          vMenu = $('#resources-menu ul');
          if (!vMenu.length) {
            vMenu = $(menuStart.replace('MENU_LABEL', 'Resources').replace('MENU_ID', 'resources-menu') + menuEnd).appendTo(navBar);
          }
          vMenu.append(mLabel);
        }

        navBar.insertBefore('.header-left');
        $('#navbar').append(navBar).append(buttonsData);
        $('.nav-menu > li > a').click(function () {
          $(this).next('ul').toggle();
          return false;
        });
      },
      handleRightMenu: function () {
        var pagePath = location.pathname.toLowerCase();
        var pagePrefix = pagePath.split('/')[1];
        if (pagePrefix) {
          var activeMenu = $('#right-menu .sidenav li[data-page="' + pagePrefix + '"]');
          if (activeMenu.length) {
            activeMenu.addClass('active').parents('li').addClass('active');
          }
        }
      },
      selectMenuItems: function () {
        var pagePath = location.pathname.toLowerCase();
        var pagePrefix = pagePath.split('/')[1];
        $('#right-menu .sidenav li[data-page="' + pagePrefix + '"]').addClass('selected');
      }
    }
  };

  function debug(msg) {
    if (_self.props.debug) {
      console.log(msg);
    }
  }

  _self.version();
  _self.init();

})(window);
