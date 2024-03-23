<template>
  <div class="container">
    <hr>
    <h2>{{ $t("create-course.title") }}</h2>
    <hr>
    <form @submit.prevent="creerCours" style="margin-top: 10px;">
      <div class="form-group">
        <label for="courseId">{{ $t("create-course.id") }}</label>
        <input type="text" id="courseId" v-model="newCourse.courseId" required class="center-input">
      </div>
      <div class="form-group">
        <label for="credits">{{ $t("create-course.credits") }}</label>
        <input type="number" id="credits" v-model="newCourse.credits" required class="center-input" step="1" min="0">
      </div>
      <div class="form-group">
        <label for="title">{{ $t("create-course.course-name") }}</label>
        <input type="text" id="title" v-model="newCourse.title" required class="center-input">
      </div>
      <button type="submit" @click="createCourse(newCourse)">{{ $t("create-course.create") }}</button>
    </form>
    <PopUp :showPopup="showPopup" v-if="showPopup" @close="showPopup = false" :message="popupMessage" />
  </div>
</template>

<script>
import api from "@/plugins/api"
import store from "@/plugins/store"
import PopUp from "@/components/PopUp.vue"
export default {
  components:{
    PopUp
  },
  data() {
    return {
      newCourse: {
        courseId: '',
        credits: '',
        title: '',
      },
      showPopup: false,
      popupMessage: ""
    };
  },
  methods: {
    async createCourse(newCourse){
      const dataTosend =  newCourse.courseId.trim() && !isNaN(newCourse.credits) && newCourse.title.trim()
        if (dataTosend) {
          try {
            const matricule = store.getters.userData.matricule
            const response = await api.post(`/courses/add?matricule=${matricule}`, {
              courseId: newCourse.courseId,
              title: newCourse.title,
              credits: newCourse.credits
            });
            newCourse.courseId = ''
            newCourse.credits = ''
            newCourse.title = ''
            console.log(response.data)
            this.popupMessage = response.data
            this.showPopup = true
          } catch (error) {
            console.error(error)
          }
      }
    }
  }
  
};
</script>

<style lang="scss" scoped>
  .container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 5px;
  
  }

  .form-group {
    margin-bottom: 15px;
  }

  .center-input {
    width: calc(100% - 16px);
    padding: 8px;
    font-size: 16px;
    border-radius: 5px;
    border: 1px solid #ccc;
    margin: 0 auto;
    display: block;
  }

  label {
    display: block;
    margin-bottom: 5px;
  }

  button[type="submit"] {
    padding: 10px 20px;
    font-size: 16px;
    background-color: #D9D9D9;
    color: #000;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-family: "Kode Mono", monospace;
    transition: background-color 0.4s ease-in-out;
  }
  

  button[type="submit"]:hover {
    background-color: #AFAFAF;
  }
    

  
</style>
