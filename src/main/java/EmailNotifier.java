import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import io.github.cdimascio.dotenv.Dotenv;

public class EmailNotifier {
    private final Dotenv dotenv = Dotenv.load();
    private final String sender = dotenv.get("SENDER_EMAIL");
    private final String email_token = dotenv.get("EMAIL_PASS");

    private Mailer mailer;

    public EmailNotifier() {
        this.mailer = MailerBuilder
                .withSMTPServer(
                        "smtp.gmail.com",
                        587,
                        sender,
                        email_token
                )
                .buildMailer();

    }

    public boolean notify(String to, boolean buildStatus, String commitID, String logURL) {

        String htmlContent = "<h1>Your results for commit: " + commitID + "</h1>" +
                "<p>Build status: " + buildStatus + "</p>" +
                "<p>Log URL: " + logURL + "</p>";

        Email email = EmailBuilder.startingBlank()
                .from("Cindy Serving <" + sender +">")
                .to(to)
                .withSubject("HTML Email Example")
                .withHTMLText(htmlContent)
                .buildEmail();

        this.mailer.sendMail(email);

        return true;
    }

}
