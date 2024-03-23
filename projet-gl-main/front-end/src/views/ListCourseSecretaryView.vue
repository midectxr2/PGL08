<template>
  <div>
    <ListCourse>
      <template #buttons="{ course, toggleEditCredits, toggleEditTeacher, toggleDelete }">
        <div v-if="!course.editingCredits && !course.editingTeacher && !course.delete">
          <button class="btn" @click="toggleEditCredits(course)">{{ $t("list-course.modify-credits") }}</button>
          <button class="btn other-btn" @click="toggleDelete(course)">{{ $t("list-course.delete") }}</button>
          <button class="btn other-btn" @click="toggleEditTeacher(course)">{{ $t("list-course.affect-teacher") }}</button>
        </div>
        <div v-else-if="course.editingCredits">
          <button type="submit" class="btn" @click="sendCredits(course)">{{ $t("list-course.confirm") }}</button>
          <button class="btn other-btn" @click="cancelEdit(course)">{{ $t("list-course.cancel") }}</button>
        </div>
        <div v-else-if="course.editingTeacher">
          <button type="submit" class="btn" @click="affectTeacher(course)">{{ $t("list-course.confirm") }}</button>
          <button class="btn other-btn" @click="cancelEdit(course)">{{ $t("list-course.cancel") }}</button>
        </div>
        <div v-else>
          <span class="info-label">{{ $t("list-course.sure") }}</span>
          <button class="btn other-btn" @click="deleteCourse(course)">{{ $t("list-course.confirm") }}</button>
          <button class="btn other-btn" @click="cancelEdit(course)">{{ $t("list-course.cancel") }}</button>
        </div>
      </template>
    </ListCourse>
    <PopUp :showPopup="showPopup" v-if="showPopup" @close="showPopup = false" :message="popupMessage" />
  </div>
</template>

<script>
import ListCourse from '@/components/ListCourse.vue'
import api from "@/plugins/api"
import store from "@/plugins/store"
import { ref } from 'vue'
import PopUp from '@/components/PopUp.vue'

export default {
  components: {
    ListCourse,
    PopUp
  },

  setup() {
    const courseList = ref([])
    const showPopup = ref(false)
    const popupMessage = ref("")

    async function sendCredits(course) {
      try {
        const creditsToSend = parseFloat(course.newCredits)
        if (isNaN(creditsToSend)){
          popupMessage.value = "Credits must not be empty"
          showPopup.value = true
          cancelEdit(course)
          return
        }
        const matricule = store.getters.userData.matricule
        const response = await api.patch(`/courses/modify?matricule=${matricule}`, {
          courseId: course.courseId,
          credits: course.newCredits
        })
        console.log(response.data)
        popupMessage.value = "Credits modified"
        showPopup.value = true
        course.editingCredits = false
      } catch (error) {
        console.error(error)
      }
    }

    function cancelEdit(course) {
      if (course.editingCredits){
        course.editingCredits = false
      }else if(course.editingTeacher){
        course.editingTeacher = false
      }else{
        course.delete = false
      }
    }

    async function deleteCourse(course) {
      try {
        const matricule = store.getters.userData.matricule
        const response = await api.delete(`/courses/delete?matricule=${matricule}&courseId=${course.courseId}`)
        console.log(response.data)
        popupMessage.value = response.data
        showPopup.value = true
        cancelEdit(course)
      } catch (error) {
        console.error(error)
      }
    }

    async function affectTeacher(course){
      try {
        const teacherMatriculeString = String(course.teacherMatricule)
        if (!course.teacherMatricule || teacherMatriculeString.length !== 6) {
          popupMessage.value = "Teacher's matricule must be six digits"
          showPopup.value = true
          cancelEdit(course)
          return
        }
        const response = await api.post(`/teachers/${course.teacherMatricule}/affect?courseId=${course.courseId}`)
        console.log(response.data)
        popupMessage.value = response.data
        showPopup.value = true
        course.editingTeacher = false
      } catch (error) {
        console.error(error)
        popupMessage.value = error.response.data
        showPopup.value = true
      }
    }
    
    return {
      courseList,
      sendCredits,
      cancelEdit,
      deleteCourse,
      affectTeacher,
      showPopup,
      popupMessage
    }
  }
};
</script>

<style>
.btn {
  padding: 5px 10px;
  background-color: #ccc;
  border: none;
  border-radius: 5px;
  color: #000;
  cursor: pointer;
  margin-left: auto;
  font-family: "Kode Mono", monospace;
  transition : background-color 0.3s ease-in-out;
}

.btn:hover{
  background-color: #AFAFAF;
}

.other-btn {
  margin-left: 10px;
}
</style>
