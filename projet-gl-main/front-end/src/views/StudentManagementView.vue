<template>
  <div class="container">
    <hr>
    <h1>{{ $t("student-management.title") }}</h1>
    <hr>
    <ul class="stList">
      <li
        v-for="(student, index) in studentList"
        :key="index"
        class="student"
      >
        {{ student.matricule }}
        {{ student.firstname }}
        {{ student.lastname }}
        {{ student.registrationStatus }}
      </li>
    </ul>
  </div>
</template>

<script>
import {onMounted, ref} from "vue";
import api from "@/plugins/api.js";

export default {
  setup() {

    const studentList = ref([])

    async function fetchStudentAllStudent() {
      const response = await api.get("/users?userRole=STUDENT")
      console.log(response.data)

      studentList.value = response.data
    }

    onMounted(fetchStudentAllStudent)

    return {
      studentList
    }
  }
}
</script>

<style lang="scss" scoped>
li {
  list-style: none;
  transition: background-color 0.3s; 

}

.container{
  max-width: 100%;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}

.stList li {
  border: 1px solid black;
  margin: 3%;
  padding: 15px;
  border-radius: 5px;
  transition: border-width 0.3s, box-shadow 0.3s;
}
.stList li:hover{
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
}
</style>
