export default {
  install(Vue) {
    Vue.mixin({
      directives: {
        loadmore: {
          bind(el, binding) {
            window.setTimeout(function () {
              let selectWrap = el.querySelector(".ant-table-body");
              if (!selectWrap) selectWrap = el.querySelector(".el-table__body-wrapper");
              var lastScrollTop = 0;
              selectWrap.addEventListener("scroll", function () {
                let sign = 200;
                if (lastScrollTop != this.scrollTop) {
                  lastScrollTop = this.scrollTop;
                  const scrollDistance = this.scrollHeight - this.scrollTop - this.clientHeight;
                  if (scrollDistance <= sign) {
                    binding.value();
                  }
                }
              });
            }, 2000);
          }
        }
      }
    });
  }
};