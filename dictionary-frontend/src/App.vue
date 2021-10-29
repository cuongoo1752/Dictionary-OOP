<template>
  <v-app id="inspire">
    <v-system-bar app>
      <v-spacer></v-spacer>

      <v-icon>mdi-square</v-icon>

      <v-icon>mdi-circle</v-icon>

      <v-icon>mdi-triangle</v-icon>
    </v-system-bar>
    <!-- Navbar -->
    <v-navigation-drawer v-model="drawer" app>
      <v-sheet color="grey lighten-4" class="pt-6 pb-0 px-2">
        <!-- <v-avatar class="mb-4" color="grey darken-1" size="64"></v-avatar> -->

        <v-text-field
          label="Tìm kiếm"
          placeholder="Nhập từ Tiếng Anh cần tìm!"
          outlined
          v-model="textSearch"
          @keyup="searchTimeOut()"
          @keyup.enter="words.length > 0 ? (wordDetail = words[0]) : 1"
        ></v-text-field>
      </v-sheet>

      <!-- <v-divider></v-divider> -->

      <!-- List Words -->
      <v-list>
        <v-list-item v-for="word in words" :key="word.id" link>
          <v-list-item-content @click="wordDetail = word">
            <v-list-item-title class="font-weight-bold">{{
              word.word
            }}</v-list-item-title>
            <span>{{ word.description }}</span>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-container class="py-8 px-6" fluid>
        <v-row>
          <v-col>
            <v-card class="content">
              <!-- Group Button -->
              <!-- Dialog -->
              <v-dialog v-model="dialog" width="500">
                <template v-slot:activator="{ on, attrs }">
                  <div class="edit_delete mx-2 my-3">
                    <v-btn
                      class="mx-2"
                      small
                      fab
                      dark
                      color="cyan"
                      v-bind="attrs"
                      v-on="on"
                      @click="
                        diglogWord = wordDetail;
                        stateDialog = 'update';
                      "
                    >
                      <v-icon dark>
                        mdi-pencil
                      </v-icon>
                    </v-btn>
                    <v-btn
                      class="mx-2"
                      small
                      fab
                      dark
                      color="cyan"
                      v-bind="attrs"
                      v-on="on"
                      @click="
                        diglogWord = newWord;
                        stateDialog = 'add';
                      "
                    >
                      <v-icon dark>
                        mdi-plus
                      </v-icon>
                    </v-btn>
                    <v-btn
                      class="mx-2"
                      small
                      fab
                      dark
                      color="red"
                      v-bind="attrs"
                      v-on="on"
                      @click="stateDialog = 'delete'"
                    >
                      <v-icon dark>
                        mdi-trash-can
                      </v-icon>
                    </v-btn>
                  </div>
                </template>

                <v-card>
                  <v-card-title
                    v-if="stateDialog == 'update'"
                    class="text-h5 grey lighten-2"
                  >
                    Sửa từ
                  </v-card-title>
                  <v-card-title
                    v-else-if="stateDialog == 'add'"
                    class="text-h5 grey lighten-2"
                  >
                    Thêm từ mới
                  </v-card-title>
                  <v-card-title v-else class="text-h5 grey lighten-2">
                    Xóa từ
                  </v-card-title>

                  <v-card-text class="py-5">
                    <template v-if="stateDialog != 'delete'">
                      <v-form ref="form" v-model="valid" lazy-validation>
                        <v-text-field
                          v-model="diglogWord.word"
                          :rules="[
                            (v) => !!v || 'Từ Tiếng Anh không được để trống!',
                          ]"
                          label="Từ Tiếng Anh"
                          required
                        ></v-text-field>

                        <v-text-field
                          v-model="diglogWord.description"
                          :rules="[
                            (v) =>
                              !!v || 'Nghĩa Tiếng Việt không được để trống!',
                          ]"
                          label="Nghĩa Tiếng Việt"
                          required
                        ></v-text-field>
                        <v-text-field
                          v-model="diglogWord.pronounce"
                          label="Phát âm"
                        ></v-text-field>

                        <v-btn class="mb-5" @click="generateHTML()">
                          Sinh HTML
                        </v-btn>
                        <v-textarea
                          v-model="diglogWord.html"
                          label="HTML"
                        ></v-textarea>

                        <v-spacer></v-spacer>

                        <v-btn
                          :disabled="!valid"
                          color="success"
                          class="mr-4"
                          @click="
                            dialog = false;
                            submitFormAddAndUpate();
                          "
                        >
                          Chấp nhận
                        </v-btn>

                        <v-btn
                          color="error"
                          @click="dialog = false"
                          class="mr-4"
                        >
                          Hủy
                        </v-btn>
                      </v-form>
                    </template>
                    <template v-else>
                      <h3 class="font-weight-bold">
                        Bạn có muốn xóa từ
                        <span style="color:red"
                          >{{ wordDetail.word }}</span
                        >
                        không?
                      </h3>
                      <v-btn
                        color="error"
                        @click="
                          dialog = false;
                          deleteWord();
                        "
                        class="mr-4 mt-5"
                      >
                        Có
                      </v-btn>
                      <v-btn
                        color="success"
                        @click="dialog = false"
                        class="mr-4 mt-5"
                      >
                        Không
                      </v-btn>
                    </template>
                  </v-card-text>

                  <v-divider></v-divider>
                </v-card>
              </v-dialog>

              <!-- Content -->
              <div class="pl-5 pt-2 pb-5">
                <v-btn
                  v-if="wordDetail.id > 0"
                  @click="playTextToSpeech()"
                  small
                  fab
                >
                  <v-icon dark>
                    mdi-volume-high
                  </v-icon>
                </v-btn>
                <div v-html="wordDetail.html"></div>
              </div>
              <!-- Snackbar -->
              <v-snackbar v-model="snackbar" :color="colorSnackbar" :timeout="timeout">
                {{ textSnackbar }}
              </v-snackbar>

              <div class="text-center"></div>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import axios from "axios";
export default {
  name: "App",

  components: {},
  created(){
    this.wordDetail = this.homeWord;
  },

  data: () => ({
    words: [],

    // Đối tượng hiện tại và đối tượng khi sửa
    wordDetail:{},
    homeWord: {
      id: 0,
      description: "",
      html: '<h1 class="my-4">  ỨNG DỤNG TỪ ĐIỂN</h1><p><i class="my-5">~ Nhanh chóng, dễ dàng, thuận tiện ~</i></p>',
      pronounce: "Tra cứu nhanh, thuận tiện, chính xác!",
      word: "ỨNG DỤNG TỪ ĐIỆN",
    },

    // Đối tượng khởi tạo thêm mới
    newWord: {
      description: "",
      html:
        "<h1>Từ tiếng anh</h1><h3><i>/Phiên âm/</i></h3><h2>Loại từ</h2><ul><li>Nghĩa</li></ul>",
      pronounce: "",
      word: "",
    },

    diglogWord: {},

    // Trạng thái của của số Dialog - add, update, delete
    stateDialog: "add",
    dialog: false,
    valid: true,

    // Trường từ khóa tìm kiếm
    textSearch: "",
    oldTextSearch: "",

    //URL Server
    URL: "http://localhost:8081/api/v1/Words",

    //URL Audio
    urlAudio:
      "https://translate.google.com/translate_tts?ie=UTF-8&q=Hello%20World&tl=en&total=1&idx=0&textlen=11&client=tw-ob&prev=input&ttsspeed=1",

    //Snackbar
    snackbar: false,
    textSnackbar: "Thêm mới thành công",
    timeout: 1200,
    colorSnackbar: "success",

    //Từ cũ
    cards: ["Today"],
    drawer: null,
    links: [
      ["mdi-inbox-arrow-down", "Inbox"],
      ["mdi-send", "Send"],
      ["mdi-delete", "Trash"],
      ["mdi-alert-octagon", "Spam"],
    ],
  }),
  methods: {
    /**
     * Hàm tìm kiếm có thời gian trể nhỏ
     * Created By: LMCUONG(28/10/2021)
     */
    searchTimeOut() {
      if (this.timer) {
        clearTimeout(this.timer);
        this.timer = null;
      }
      this.timer = setTimeout(() => {
        this.getWords("get");
      }, 200);
    },

    /**
     * Lấy từ khóa khi nhập dự liệu
     * Created By: LMCUONG(28/10/2021)
     */
    getWords(state) {
      if (this.isOtherWord() || state == "refresh")
        axios
          .get(this.URL + "/" + this.textSearch)
          .then((response) => {
            this.words = response.data.data;
            this.oldTextSearch = this.textSearch;
          })
          .catch((e) => {
            console.log(e);
          });
    },

    /**
     * Kiểm tra xem từ nhập vào khác từ cũ không
     * @param {}
     * @returns {}
     * Created By: LMCUONG(28/10/2021)
     */
    isOtherWord() {
      let isOther = true;

      if (this.textSearch == "" || this.textSearch == null) {
        isOther = false;
      }

      if (isOther) {
        if (this.oldTextSearch == this.textSearch) {
          isOther = false;
        }
      }

      return isOther;
    },

    playTextToSpeech() {
      this.speak(this.wordDetail.word);
    },

    /**
     * Phát audio theo từ
     * @param {}
     * @returns {}
     * Created By: LMCUONG(28/10/2021)
     */
    speak(message) {
      var msg = new SpeechSynthesisUtterance(message);
      var voices = window.speechSynthesis.getVoices();
      msg.voice = voices[4];
      window.speechSynthesis.speak(msg);
    },

    submitFormAddAndUpate() {
      if (this.stateDialog == "add") {
        this.addWord();
      } else if (this.stateDialog == "update") {
        this.updateWord();
      }
    },

    addWord() {
      axios({
        method: "post",
        url: this.URL + "/insert",
        headers: {},
        data: this.diglogWord,
      })
        .then((response) => {
          this.handleResponse(response.data.status, response.data.message);
          this.newWord = {
            description: "",
            html:
              "<h1>Từ tiếng anh</h1><h3><i>/Phiên âm/</i></h3><h2>Loại từ</h2><ul><li>Nghĩa</li></ul>",
            pronounce: "",
            word: "",
          };
        })
        .catch((e) => {
          this.openSnackbar(e.response, "error");
          console.log(e);
        });
    },

    updateWord() {
      axios({
        method: "put",
        url: this.URL + "/update",
        headers: {},
        data: this.diglogWord,
      })
        .then((response) => {
          this.handleResponse(response.data.status, response.data.message);
        })
        .catch((e) => {
          this.openSnackbar(e.response, "error");
          console.log(e);
        });
    },

    deleteWord() {
      axios({
        method: "delete",
        url: this.URL + "/" + this.wordDetail.id,
      })
        .then((response) => {
          this.handleResponse(response.data.status, response.data.message);
          this.wordDetail = this.homeWord;
        })
        .catch((e) => {
          this.openSnackbar(e.response, "error");
          console.log(e);
        });
    },

    // Xử lý Snackbar
    handleResponse(status, message) {
      if (status == "OK") {
        this.openSnackbar(message, "success");
        this.getWords("refresh");
      } else {
        this.openSnackbar(message, "error");
      }
    },

    openSnackbar(text, state) {
      this.snackbar = true;
      this.colorSnackbar = state;
      this.textSnackbar = text;
    },

    /**
     * Sinh mã HTML
     * @param {}
     * @returns {}
     * Created By: LMCUONG(28/10/2021)
     */
    generateHTML() {
      this.diglogWord.html = `<h1>${this.diglogWord.word}</h1>
      <h3><i>/${this.diglogWord.pronounce}/</i></h3>
      <ul><li>${this.diglogWord.description}</li></ul>`;
      console.log(this.diglogWord);
    },
  },
};
</script>
<style scoped>
h1 {
  margin: 30px;
}
.edit_delete {
  position: absolute;
  right: 0;
}
</style>
