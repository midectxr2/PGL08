<template>
  <h1 ref="animatedText" class="line-1 anim-typewriter"><span class="sys-class">Console</span><span>.</span><span class="function">log</span>
    <span class="ponctuation">(</span><span class="string">"Welcome to NASA University"</span><span
      class="ponctuation">)</span><span>;</span>
  </h1>
</template>

<script setup>
import {onMounted, ref} from 'vue'

let animatedText = ref(null)

const animationTime = 3.5
const blinkTime = 500

function startAnimation() {
  animatedText.value.style.animation = `typewriter ${animationTime}s steps(44) 1s 1 normal both, blinkTextCursor ${blinkTime}ms steps(44) 10000 normal`

  setTimeout(() => {
    endAnimation()
  }, 6000);
}

function endAnimation() {
  const elements = document.querySelectorAll('.line-1 span:not(.string)')
  elements.forEach(element => element.remove())
  const stringElement = document.querySelector('.line-1 .string')

  stringElement.textContent = stringElement.textContent.replace(/"/g, '')
  animatedText.value.getAnimations().forEach((animation) => animation.finish())


  animatedText.value.style.display = 'flex'
  animatedText.value.style.justifyContent = 'center'
  animatedText.value.style.alignItems = 'center'
  animatedText.value.style.fontSize = '25px'
}

onMounted(startAnimation)
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inconsolata:wght@200..900&display=swap');

@font-face {
  font-family: "Droid Sans Mono";
  src: url("@/assets/fonts/droid-sans-mono/DroidSansMono.ttf") format('opentype');
}

.sys-class {
  color: #0099ff;
}

.sys-cmd {
  color: white;
}

.function {
  color: #ffbf00;
}

.string {
  color: #ff6600;
}

.ponctuation {
  color: #ff66cc;
}

.line-1 {
  color: white;
  position: relative;
  margin: 0 auto;
  border-right: 2px solid rgba(255, 255, 255, 0);
  font-size: 100%;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  font-family: "Droid Sans Mono", "Inconsolata", "Kode Mono", monospace;
}

@keyframes typewriter {
  from {
    width: 0;
  }

  to {
    width: 25.3em;
  }
}

@keyframes blinkTextCursor {
  from {
    border-right-color: rgba(255, 255, 255, .75);
  }

  to {
    border-right-color: transparent;
  }
  100% {
    border-right-color: transparent;
  }
}
</style>
