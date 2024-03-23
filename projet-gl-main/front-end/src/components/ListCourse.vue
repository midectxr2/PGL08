<template>
  <div class="container">
    <hr/>
    <h2>{{ $t("list-course.title") }}</h2>
    <hr/>
    <button class="btn refresh" @click="fetchCourseList()">{{ $t("list-course.refresh") }}</button>
    <ul class="course-list">
      <li v-for="(course, index) in courseList" :key="index" class="course-item">
        <div class="course-info">
          <div class="info-row" >
            <span class="info-label">{{ $t("list-course.id") }}</span>
            <span>{{ course.courseId }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">{{ $t("list-course.credits") }}</span>
            <span v-if="!course.editingCredits">{{ course.credits }}</span>
            <input v-else v-model="course.newCredits" type="number" min="0" style="width: 40px;"/>
          </div>
          <div class="info-row">
            <span class="info-label">{{ $t("list-course.name") }}</span>
            <span v-if="!course.editingTitle">{{ course.title }}</span>
            <input v-else v-model="course.newTitle" type="text"/>
          </div>
          <div v-if="course.editingTeacher" class="info-row">
            <span class="info-label">{{ $t("list-course.teacher") }}</span>
            <input type="number" v-model="course.teacherMatricule" step="1" min="1" style="width: 60px;"/>
          </div>
          <div class="button-container">
            <slot name="buttons" v-bind="{ course, toggleEditCredits, toggleEditTitle, toggleEditTeacher, toggleDelete}"></slot>
          </div>
        </div>
      </li>
    </ul>
    
  </div>
</template>

<script>
import api from "@/plugins/api"
import store from "@/plugins/store"
import { ref, onMounted } from 'vue'

export default {
  setup() {
    const courseList = ref([])

    async function fetchCourseList() {
      try {
        const matricule = store.getters.userData.matricule
        const response = await api.get(`/courses/${matricule}`)
        courseList.value = response.data
        console.log(courseList.value)
      } catch (error) {
        console.error(error);
      }
    }

    function toggleEditCredits(course) {
      course.editingCredits = !course.editingCredits;
      if (!course.editingCredits) {
        course.newCredits = course.credits;
      }
    }

    function toggleEditTitle(course) {
      course.editingTitle = !course.editingTitle;
      if (!course.editingTitle) {
        course.newTitle = course.title;
      }
    }

    function toggleEditTeacher(course) {
      course.editingTeacher = !course.editingTeacher;
    }

    function toggleDelete(course){
      course.delete = !course.delete
    }


    onMounted(fetchCourseList)
    
    return {
      courseList,
      fetchCourseList,
      toggleEditCredits,
      toggleEditTitle,
      toggleEditTeacher,
      toggleDelete
    }
  }
  
};
</script>

<style lang="scss" scoped>

  .refresh {
    margin-top: 5px;
  }
  
  .container {
    max-width: 100%;
    margin: 0 auto;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 5px;
  }

  .course-list {
    list-style-type: none;
    padding: 0;
  }

  .course-item {
    margin-bottom: 20px;
  }

  .course-info {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    background-color: #fff;
    display: flex;
    flex-wrap: wrap;
  }

  .info-row {
    display: flex;
    align-items: center;
    margin-right: 10px;
  }

  .info-label {
    font-weight: bold;
    margin-right: 5px;
  }

  .button-container {
    margin-left: auto;
    display: grid;
    grid-auto-flow: column;
    gap: 10px;
  }

</style>
