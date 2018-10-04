(function (w) {
  var _self = {
    props: {
      version: '1.0',
      prefix: 'TOPNAV:::',
      debug: true,
      isIframe: window.self !== window.top
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
      _self.ready();
    },
    ready: function () {
      $(function () {
        debug('Registering events');
        _self.events.scroll();

        debug('Calling methods');
        var id = setInterval(function () {
          if (!$('.sideContent .menu .selected').length) return;
          clearInterval(id);
          _self.methods.sidebar();
        }, 500);
        _self.methods.breadcrumb();
        console.log('MENU:: Start');
        _self.methods.generateVersionMenu();
        console.log('MENU:: End');
      });
    },
    methods: {
      breadcrumb: function () {
        $('.breadcrumbs').prependTo('.main-section');
      },
      sidebar: function () {
        $('.sideContent .selected-child-parent').removeClass('selected-child-parent');
        $('.sideContent .selected-child-menu').removeClass('selected-child-menu');
        $('.sideContent .selected-child').removeClass('selected-child');

        $('.sideContent .selected').parent().addClass('selected-child');
        $('.sideContent .selected-child').parents('ul:not(.menu)').addClass('selected-child-menu');
        $('.sideContent .selected-child').parents('ul:not(.menu)').last().prev().addClass('selected-child-parent');
      },
      generateVersionMenu: function () {
        _self.utils.getFile({
          callback: _self.methods.buildVersionMenu,
          filename: '/custom.menu.js'
        });
      },
      buildVersionMenu: function () {
        var mList;
        var curr = location.href.match(/\/(\d+?[\.\d]*?)\//)[1];
        var versionMenuStart = '<ul class="version-menu"><li><a href="#">' + curr + '<span class="caret"></span></a><ul>';
        var versionMenuEnd = '</ul></li></ul>';

        debug('buildVersionMenu::versionData:', versionData);
        mList = versionMenuStart;
        for (var v in versionData) {
          if (v == curr)
            mList += '<li><a href="#" onClick="return false;">' + v + '</a></li>';
          else
            mList += '<li><a href="' + versionData[v] + '">' + v + '</a></li>';
        }
        mList += versionMenuEnd;
        debug('buildVersionMenu::mList:', mList);

        $(mList).appendTo('.logo-wrapper');
        $('.version-menu>li>a')
          .click(function (e) {
            e.preventDefault();
            $('.version-menu>li').toggleClass('open');
          }).blur(function (e) {
            e.preventDefault();
            $('.version-menu>li').removeClass('open');
          });
      }
    },
    events: {
      scroll: function () {
        $('.body-container').scroll(_self.methods.sidebar);
      }
    },
    utils: {
      getVar: function (vname) {
        return $(vname).text();
      },
      getFile: function (options) {
        debug('getFile::protocol:', location.protocol);
        if (location.protocol == 'file:') {
          debug('getFile::protocol:Aborting get file on local files system');
          return false;
        }

        options.filename = options.filename || '';
        options.callback = options.callback || function () {};
        debug('getFile::options:', options);

        debug('getFile::filename:', options.filename);
        $.getScript(options.filename)
          .done(function () {
            options.callback(options);
          })
      }
    }
  };
  var debug = _self.log;
  w.TopNav = _self;
  _self.init();
})(window || {});