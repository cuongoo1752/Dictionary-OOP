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
                        stateDiglog = 'update';
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
                        stateDiglog = 'add';
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
                  <v-card-title class="text-h5 grey lighten-2">
                    Thêm từ mới
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
                        <v-text-field
                          v-model="diglogWord.html"
                          label="HTML"
                        ></v-text-field>

                        <v-spacer></v-spacer>
                        <v-btn
                          :disabled="!valid"
                          color="success"
                          class="mr-4"
                          @click="dialog = false"
                        >
                          Thêm từ mới
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
                          >{{ wordDetail.word }}:
                          {{ wordDetail.description }}</span
                        >
                        không?
                      </h3>
                      <v-btn
                        color="error"
                        @click="dialog = false"
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
              <div v-html="wordDetail.html" class="pl-5 pt-2 pb-5"></div>

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

  data: () => ({
    words: [
      {
        id: 29354,
        word: "love",
        html:
          "<h1>love</h1><h3><i>/lʌv/</i></h3><h2>danh từ</h2><ul><li>lòng yêu, tình thương<ul style=\"list-style-type:circle\"><li>love of one's country:<i> lòng yêu nước</i></li><li>a mother's love for her children:<i> tình mẹ yêu con</i></li></ul></li><li>tình yêu, ",
        description: "danh từ: lòng yêu, tình thương",
        pronounce: "lʌv",
      },
      {
        id: 29355,
        word: "love-affair",
        html:
          "<h1>love-affair</h1><h3><i>/'lʌvə,feə/</i></h3><h2>danh từ</h2><ul><li>chuyện yêu đương, chuyện tình</li></ul>",
        description: "danh từ: chuyện yêu đương, chuyện tình",
        pronounce: "'lʌvə,feə",
      },
      {
        id: 29356,
        word: "love-apple",
        html:
          "<h1>love-apple</h1><h3><i>/'lʌv,æpl/</i></h3><h2>danh từ</h2><ul><li>(thực vật học) cà chua</li></ul>",
        description: "danh từ: (thực vật học) cà chua",
        pronounce: "'lʌv,æpl",
      },
      {
        id: 29357,
        word: "love-begotten",
        html:
          "<h1>love-begotten</h1><h3><i>/'lʌvbi'gɔtn/</i></h3><h2>tính từ</h2><ul><li>đẻ hoang</li></ul>",
        description: "tính từ: đẻ hoang",
        pronounce: "'lʌvbi'gɔtn",
      },
      {
        id: 29358,
        word: "love-bird",
        html:
          "<h1>love-bird</h1><h3><i>/'lʌvbə:d/</i></h3><h2>danh từ</h2><ul><li>(động vật học) vẹt xanh</li></ul>",
        description: "danh từ: (động vật học) vẹt xanh",
        pronounce: "'lʌvbə:d",
      },
      {
        id: 29359,
        word: "love-child",
        html:
          "<h1>love-child</h1><h3><i>/'lʌvtʃaild/</i></h3><h2>danh từ</h2><ul><li>con hoang</li></ul>",
        description: "danh từ: con hoang",
        pronounce: "'lʌvtʃaild",
      },
      {
        id: 29360,
        word: "love-knot",
        html:
          "<h1>love-knot</h1><h3><i>/'lʌvnɔt/</i></h3><h2>danh từ</h2><ul><li>nơ thắt hình số 8</li></ul>",
        description: "danh từ: nơ thắt hình số 8",
        pronounce: "'lʌvnɔt",
      },
      {
        id: 29361,
        word: "love-letter",
        html:
          "<h1>love-letter</h1><h3><i>/'lʌv,letə/</i></h3><h2>danh từ</h2><ul><li>thư tình</li></ul>",
        description: "danh từ: thư tình",
        pronounce: "'lʌv,letə",
      },
      {
        id: 29362,
        word: "love-lorn",
        html:
          "<h1>love-lorn</h1><h3><i>/'lʌvlɔ:n/</i></h3><h2>tính từ</h2><ul><li>sầu muộn vì tình; thất tình; bị tình phụ, bị bỏ rơi</li></ul>",
        description:
          "tính từ: sầu muộn vì tình; thất tình; bị tình phụ, bị bỏ rơi",
        pronounce: "'lʌvlɔ:n",
      },
      {
        id: 29363,
        word: "love-lornness",
        html:
          "<h1>love-lornness</h1><h3><i>/'lʌvlɔ:nnis/</i></h3><h2>danh từ</h2><ul><li>nỗi sầu muộn vì tình; nỗi thất tình; sự bị tình phụ</li></ul>",
        description:
          "danh từ: nỗi sầu muộn vì tình; nỗi thất tình; sự bị tình phụ",
        pronounce: "'lʌvlɔ:nnis",
      },
      {
        id: 29364,
        word: "love-making",
        html:
          "<h1>love-making</h1><h3><i>/'lʌv,meikiɳ/</i></h3><h2>danh từ</h2><ul><li>sự tỏ tình, sự tán gái</li><li>sự ăn nằm với nhau, sự giao hợp</li></ul>",
        description: "danh từ: sự tỏ tình, sự tán gái",
        pronounce: "'lʌv,meikiɳ",
      },
      {
        id: 29365,
        word: "love-match",
        html:
          "<h1>love-match</h1><h3><i>/'lʌvmætʃ/</i></h3><h2>danh từ</h2><ul><li>sự lấy nhau vì tình</li></ul>",
        description: "danh từ: sự lấy nhau vì tình",
        pronounce: "'lʌvmætʃ",
      },
      {
        id: 29366,
        word: "love-token",
        html:
          "<h1>love-token</h1><h3><i>/'lʌv,toukən/</i></h3><h2>danh từ</h2><ul><li>vật kỷ niệm tình yêu</li></ul>",
        description: "danh từ: vật kỷ niệm tình yêu",
        pronounce: "'lʌv,toukən",
      },
      {
        id: 29367,
        word: "lovelace",
        html:
          "<h1>lovelace</h1><h3><i>/'lʌvleis/</i></h3><h2>danh từ</h2><ul><li>chàng công tử phong lưu; anh chàng hay tán gái; sở khanh</li></ul>",
        description:
          "danh từ: chàng công tử phong lưu; anh chàng hay tán gái; sở khanh",
        pronounce: "'lʌvleis",
      },
      {
        id: 29368,
        word: "loveless",
        html:
          "<h1>loveless</h1><h3><i>/'lʌvlis/</i></h3><h2>tính từ</h2><ul><li>không tình yêu; không yêu; không được yêu</li></ul>",
        description: "tính từ: không tình yêu; không yêu; không được yêu",
        pronounce: "'lʌvlis",
      },
      {
        id: 29369,
        word: "loveliness",
        html:
          "<h1>loveliness</h1><h3><i>/'lʌvlinis/</i></h3><h2>danh từ</h2><ul><li>vẻ đẹp, vẻ đáng yêu, vẻ yêu kiều</li></ul>",
        description: "danh từ: vẻ đẹp, vẻ đáng yêu, vẻ yêu kiều",
        pronounce: "'lʌvlinis",
      },
      {
        id: 29370,
        word: "lovelock",
        html:
          "<h1>lovelock</h1><h3><i>/'ʌvlɔk/</i></h3><h2>danh từ</h2><ul><li>món tóc mai (vòng xuống ở trán hay thái dương)</li></ul>",
        description: "danh từ: món tóc mai (vòng xuống ở trán hay thái dương)",
        pronounce: "'ʌvlɔk",
      },
      {
        id: 29371,
        word: "lovely",
        html:
          "<h1>lovely</h1><h3><i>/'lʌvli/</i></h3><h2>tính từ</h2><ul><li>đẹp đẽ, xinh, đáng yêu, dễ thương, có duyên, yêu kiều</li><li>(thông tục) thú vị, vui thú, thích thú</li><li>(từ Mỹ,nghĩa Mỹ) đẹp (về mặt đạo đức)</li></ul><h2>danh từ</h2><ul><li>(từ Mỹ,nghĩa",
        description:
          "tính từ: đẹp đẽ, xinh, đáng yêu, dễ thương, có duyên, yêu kiều",
        pronounce: "'lʌvli",
      },
      {
        id: 29372,
        word: "lover",
        html:
          '<h1>lover</h1><h3><i>/\'lʌvə/</i></h3><h2>danh từ</h2><ul><li>người yêu, người ham thích, người ham chuộng, người hâm mộ<ul style="list-style-type:circle"><li>a lover of music:<i> người ham thích nhạc</i></li></ul></li><li>người yêu, người tình</li></ul>',
        description:
          "danh từ: người yêu, người ham thích, người ham chuộng, người hâm mộ",
        pronounce: "'lʌvə",
      },
      {
        id: 29373,
        word: "lovesick",
        html:
          "<h1>lovesick</h1><h3><i>/'lʌvsik/</i></h3><h2>tính từ</h2><ul><li>tương tư</li></ul>",
        description: "tính từ: tương tư",
        pronounce: "'lʌvsik",
      },
    ],

    // Đối tượng hiện tại và đối tượng khi sửa
    wordDetail: {
      id: 0,
      description: "",
      html:
        "",
      pronounce: "Tra cứu nhanh, thuận tiện, chính xác!",
      word: "ỨNG DỤNG TỪ ĐIỆN",
    },

    // Đối tượng khởi tạo thêm mới
    newWord: {
      id: -1,
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

    //URL Server
    URL: "localhost:8081/api/v1/Words",

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
    searchTimeOut() {
      if (this.timer) {
        clearTimeout(this.timer);
        this.timer = null;
      }
      this.timer = setTimeout(() => {
        // code
        axios
          .get(this.URL)
          .then((response) => {
            console.log(response);
          })
          .catch((e) => {
            console.log(e);
          });
      }, 500);
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
