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
        _self.methods.breadcrumb();
        _self.methods.sidebar();
      });
    },
    methods: {
      breadcrumb: function () {
        $('.breadcrumbs').prependTo('.main-section');
      },
      sidebar: function () {
        var id = setInterval(function () {
          if (!$('.sideContent .menu .selected').length) return;
          clearInterval(id);
          $('.sideContent .menu .selected-child').removeClass('selected-child');
          $('.sideContent .menu .selected').parent().addClass('selected-child');
        }, 500);
      }
    },
    events: {
      scroll: function () {
        _self.methods.sidebar();
      }
    },
    utils: {
      getVar: function (vname) {
        return $(vname).text();
      }
    }
  };
  var debug = _self.log;
  w.TopNav = _self;
  _self.init();
})(window || {});