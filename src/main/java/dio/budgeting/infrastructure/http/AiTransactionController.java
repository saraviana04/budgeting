package dio.budgeting.infrastructure.http;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/transactions")
@Profile("openai")
public class AiTransactionController {
    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;

    public AiTransactionController(TranscriptionModel transcriptionModel,
                                   ChatClient budgetingChatClient,
                                   TextToSpeechModel textToSpeechModel) {
        this.transcriptionModel = transcriptionModel;
        this.chatClient = budgetingChatClient;
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mpeg")
    public ResponseEntity<Resource> processVoice(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Envie um arquivo de áudio não vazio");
        }
        var userMessage = transcriptionModel.transcribe(file.getResource());
        var result = chatClient.prompt().user(userMessage).call().content();
        var audio = textToSpeechModel.call(result);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("resposta.mp3").build().toString())
                .body(new ByteArrayResource(audio));
    }
}
