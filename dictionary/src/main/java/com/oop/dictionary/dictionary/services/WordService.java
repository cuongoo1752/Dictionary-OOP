package com.oop.dictionary.dictionary.services;

import com.oop.dictionary.dictionary.models.ResponseObject;
import com.oop.dictionary.dictionary.models.Word;
import com.oop.dictionary.dictionary.repositories.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordService {
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Tìm kiếm 20 từ giống với character nhất
     * @param character các ký tự cần tìm kiếm
     * @return Kết quả khi tìm kiếm
     */
    public ResponseObject findWords(String character) {
        // Gọi Stored Procerduce ở phía Cơ sở dữ liệu
        List<Object[]> newWords = entityManager.createNamedStoredProcedureQuery("Proc_FindWords")
                .setParameter("characters", character).getResultList();

        // Chuyển các đối tượng thành kiểu Word
        List<Word> words = new ArrayList<Word>();
        for (int i = 0; i < newWords.size(); i++) {
            words.add(new Word((int) newWords.get(i)[0], (String) newWords.get(i)[4], (String) newWords.get(i)[2], (String) newWords.get(i)[1], (String) newWords.get(i)[3]));
        }

        return new ResponseObject("OK", "Lấy các từ tìm kiếm thành công!", words);
    }

    /**
     * Thêm từ mới
     * @param newWord từ mới cần thêm
     * @return Kết quả khi thêm từ mới
     */
    public ResponseObject insertWord(Word newWord) {

        // Kiểm tra các trường có trống không?
        ResponseObject vadidate = VadidateRequired(newWord);
        if(vadidate.getStatus().equals("Failed")){
            return vadidate;
        }

        // Kiểm tra từ có trong hệ thống chưa?
        List<Word> foundWord = wordRepository.findByWord(newWord.getWord().trim());

        if (foundWord.size() > 0) {
            return new ResponseObject("Failed", "Từ " + newWord.getWord() + " đã có trong hệ thống!", "");
        }

        // Lưu vào hệ thống

        return new ResponseObject("OK", "Thêm mới thành công!", wordRepository.save(newWord));
    }

    /**
     * Cập nhật từ mới
     * @param newWord từ cần cập nhật
     * @return Kết quả khi cập nhật
     */
    public ResponseObject updateWord(Word newWord) {

        // Kiểm tra các trường có trống không?
        ResponseObject vadidate = VadidateRequired(newWord);
        if(vadidate.getStatus().equals("Failed")){
            return vadidate;
        }

        // Kiểm tra từ này đã có trong hệ thống chưa
        // - Nếu có sửa từ này
        // - Nếu chưa có thêm từ mới
        Word updateWord = wordRepository.findById(newWord.getId())
                .map(word -> {
                    word.setWord(newWord.getWord());
                    word.setDescription(newWord.getDescription());
                    word.setPronounce(newWord.getPronounce());
                    word.setHtml(newWord.getHtml());
                    return wordRepository.save(word);
                }).orElseGet(() -> {
                    return wordRepository.save(newWord);
                });

        return new ResponseObject("OK", "Sửa mới thành công!", updateWord);
    }

    /**
     * Xóa từ mới theo Id
     * @param id id của từ
     * @return kết quả khi xóa
     */
    public ResponseObject deleteWord(int id) {
        // Kiểm tra từ id này có trong hệ thống không?
        boolean existWord = wordRepository.existsById(id);

        if (!existWord) {
            return new ResponseObject("Failer", "Phần tử có id " + id + " không tồn tại!", "");
        }

        // Xóa từ theo Id
        wordRepository.deleteById(id);
        return new ResponseObject("OK", "Xóa thành công!", "");
    }

    /**
     * Kiểm tra các trường có trống hay không?
     * @param word dữ liệu cần kiểm tra
     * @return Kết quả khi kiểm tra
     */
    public ResponseObject VadidateRequired(Word word){
        // Kiểm tra trống
        if (word.getWord().trim().isEmpty()) {
            return new ResponseObject("Failed", "Trường Word không được để trống", "");
        }

        if (word.getDescription().trim().isEmpty()) {
            return new ResponseObject("Failed", "Trường Nghĩa không được để trống", "");
        }

        return  new ResponseObject("OK", "", "");
    }

}
