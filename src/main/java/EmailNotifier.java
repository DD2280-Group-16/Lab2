import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import io.github.cdimascio.dotenv.Dotenv;

public class EmailNotifier {
    private final Mailer mailer;
    private final String sender;

    public EmailNotifier() {
        Dotenv dotenv = Dotenv.load();
        this.sender = dotenv.get("SENDER_EMAIL");
        this.mailer = MailerBuilder
                .withSMTPServer(
                        "smtp.gmail.com",
                        587,
                        sender,
                        dotenv.get("EMAIL_PASS"))
                .buildMailer();
    }

    public EmailNotifier(String sender, Mailer mail) {
        this.sender = sender;
        this.mailer = mail;
    }

    public boolean notify(String to, boolean buildStatus, String commitID, String logURL) {

        String htmlContent = "<h1>Your results for commit: " + commitID + "</h1>" +
                "<p>Build status: " + buildStatus + "</p>" +
                "<p>Log URL: " + logURL + "</p>";

        Email email = EmailBuilder.startingBlank()
                .from("Cindy Serving <" + sender + ">")
                .to(to)
                .withSubject("HTML Email Example")
                .withHTMLText(htmlContent)
                .buildEmail();


        System.out.println("HTML email sent!");
        
        return(performSend(email));
    }

    protected boolean performSend(Email email) {
        try {
            this.mailer.sendMail(email);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
