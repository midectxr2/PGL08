<template>
  <div class="home-container">
    <HeaderPage :msg="$t('home-page.globalPrompt') + $t(displayRole)" class="header"/>


    <div class="global">
      <LeftBarPage :buttons="currentButtonsLeft" class="left-buttons"/>
      <div class="userInfo">


      <div class="user-data-actions">
        <MyData :user-data="userData" class="myData"/>
        <hr>  
        <MyActions v-if="currentButtonsAction && currentButtonsAction.length > 0" :buttons="currentButtonsAction"/>

      </div>

    </div>
    </div>

  </div>
</template>


<script>
import {onMounted, ref, watch} from 'vue';
import HeaderPage from '../components/HeaderPage.vue';
import LeftBarPage from '@/components/LeftBarPage.vue';
import MyData from '@/components/MyData.vue';
import MyActions from "@/components/MyActions.vue";
import LanguageSwitcher from '@/components/LanguageSwitcher.vue';
import Tr from '../plugins/translation';
import store from '@/plugins/store'

export default {
  components: {
    HeaderPage,
    LeftBarPage,
    MyData,
    MyActions,
    LanguageSwitcher
  },

  setup() {
    const userData = ref(null);
    const role = ref('');
    const displayRole = ref('');


    userData.value = store.getters.userData;
    role.value = userData.value.roles[0]
    console.log(role)

    const currentButtonsLeft = ref([]);
    const currentButtonsAction = ref([]);

    const notifBtn = {id: currentButtonsLeft.value.length, label: 'Notifications', link: 'notifications'}

    const profButtonsLeft = [
      {id: 1, label: 'leftButtons.list_courses', link: 'listCourseTeacher'},
      {id: 2, label: 'leftButtons.modify_data', link: 'modify-data'},

      //{id: 2, label: 'leftButtons.manage_exemption_requests'}, extension inscription étudiant
      //{id: 3, label: 'leftButtons.manage_diploma_equivalences'}, extension inscription étudiant
      {id: 3, label: 'Notifications', link: 'notifications'}
    ];

    const studentButtonsLeft = [
      {id: 1, label: 'leftButtons.aca-infos', link: 'academic-infos'},
      {id: 2, label: 'leftButtons.check_enrollment_status', link: 'check-registration'},
      {id: 3, label: 'leftButtons.enrollment_history'},
      {id: 4, label: 'leftButtons.modify_data', link: 'modify-data'},
      {id: 7, label: 'Notifications', link: 'notifications'}
    ];

    const secrButtonsLeft = [
      {id: 1, label: 'leftButtons.list_courses', link: 'listCourseSecretary'},
      {id: 2, label: 'leftButtons.create_course', link: 'create-course'},
      {id: 3, label: 'leftButtons.modify_data', link: 'modify-data'},
      {id: 4, label: 'Notifications', link: 'notifications'}
    ];

    const regServiceButtonsLeft = [
      {id: 1, label: 'leftButtons.list_students_and_status', link: 'studentManagement'},
      {id: 2, label: 'leftButtons.procedures_management', link: 'procedureManagement'},
      //{id: 3, label: 'leftButtons.view_registration_history'},
      //{id: 4, label: 'leftButtons.view_registration_request_status'},
      //{id: 5, label: 'leftButtons.process_scholarship_requests'},
      {id: 3, label: 'leftButtons.modify_data', link: 'modify-data'},
      {id: 4, label: 'Notifications', link: 'notifications'}
    ];

    const buttonsAction = [
      {id: 1, label: 'actionButtons.messages'},
      {id: 2, label: 'actionButtons.schedule'}
    ];

    const nullButtons = null;

    function updateButtons() {
      switch (role.value) {
        case 'TEACHER':
          currentButtonsLeft.value = profButtonsLeft;
          currentButtonsAction.value = buttonsAction;
          displayRole.value = 'roles.teacher';
          break;
        case 'STUDENT':
          currentButtonsLeft.value = studentButtonsLeft;
          currentButtonsAction.value = buttonsAction;
          displayRole.value = 'roles.student';
          break;
        case 'SECRETARY':
          currentButtonsLeft.value = secrButtonsLeft;
          currentButtonsAction.value = nullButtons;
          displayRole.value = 'roles.secretary';
          break;
        case 'REGISTRATIONSERVMEMBER':
          currentButtonsLeft.value = regServiceButtonsLeft;
          currentButtonsAction.value = nullButtons;
          displayRole.value = 'roles.registration_service';
          break;
        default:
          currentButtonsLeft.value = [];
          currentButtonsAction.value = [];
          console.error("User cannot have no role")
          break;
      }
    }


    updateButtons()

    onMounted(updateButtons)
    watch(role, updateButtons)

    return {
      userData,
      currentButtonsLeft,
      buttonsAction,
      role,
      currentButtonsAction,
      displayRole,
      Tr
    };
  }
};
</script>

<style lang="scss" scoped>


.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;

  width: 95%;
}
.global{
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: flex-start;;
}

.left-buttons {
    flex :1;
    border-radius: 7px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  }
.userInfo {
  flex : 9;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  width: 70%;




  .user-data-actions {
    flex: 3;
    padding: 2%;
    margin-top: 20px;
    margin-left: 20px;
    border-radius: 7px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    
  }

  .myData{
    margin-top: 2%;
  }
}

</style>
