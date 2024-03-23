<template>
  <transition name="slide">
    <div class="popup-overlay" v-if="showPopup">
      <div class="popup-content-large">
        <p style="margin-top: 0px;">{{ message }}</p>
        <div class="time-bar-container">
          <div class="time-bar" ref="timeBar"></div>
        </div>
      </div>
    </div>
  </transition>
</template>
<script>
export default {
  props: {
    showPopup: {
      type: Boolean,
      required: true
    },
    message: {
      type: String,
      default: ""
    },
  },
  mounted() {
    this.startBarAnimation();
  },
  methods: {
    closePopup() {
      this.$emit("close");
    },
    startBarAnimation() {
      const timeBar = this.$refs.timeBar;
      timeBar.style.transition = `width ${4000}ms linear`;
      timeBar.style.width = '100%'; 
      setTimeout(() => {
        timeBar.style.width = '0%';
        setTimeout(() => {
          this.closePopup();
        }, 4000); 
      },0);
    }
  }
};
</script>

<style scoped>
.popup-overlay {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 9999;
  
}

.popup-content-large {
  background-color: white;
  padding: 40px;
  border-radius: 5px;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.5);
}

.time-bar-container {
  width: 100%;
  height: 7px;
  border: 1px solid #ccc;
  border-radius: 5px;
  overflow: hidden;
  margin-top: 20px;
}

.time-bar {
  height: 100%;
  width: 100%;
  background-color: #ff6600;
  transform-origin: left;
  transition: width 4s linear;
}
</style>
  