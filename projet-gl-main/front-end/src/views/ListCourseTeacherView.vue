<template>
  <div>
    <ListCourse>
      <template #buttons="{ course, toggleEditTitle }">
        <button v-if="!course.editingTitle" class="btn" @click="toggleEditTitle(course)">{{ $t("list-course.modify-title") }}</button>
        <div v-else>
          <button type="submit" class="btn" @click="sendTitle(course)">{{ $t("list-course.confirm") }}</button>
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
    const popupMessage = ref('')

    async function sendTitle(course) {
      try {
        const titleToSend = course.newTitle && course.newTitle.trim();
        if (!titleToSend) {
          popupMessage.value = "Title must not be empty"
          showPopup.value = true
          cancelEdit(course)
          return
        }
        const matricule = store.getters.userData.matricule
        const response = await api.patch(`/courses/modify?matricule=${matricule}`, {
          courseId: course.courseId,
          title: course.newTitle
        });
        console.log(response.data)
        popupMessage.value = "Title modified"
        showPopup.value = true
        course.editingTitle = false
      } catch (error) {
        console.error(error)
      }
    }

    function cancelEdit(course) {
      course.editingTitle = false;
    }

    return {
      courseList,
      sendTitle,
      cancelEdit,
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
  }

  .other-btn {
    margin-left: 10px;
  }
</style>







