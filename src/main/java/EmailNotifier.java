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
                        email_token)
                .buildMailer();

    }

    /**
     * Sends an HTML email notification to the pusher with results
     *
     * @param to         the pushers email address
     * @param buildStatus the status of the build (true for success, false for failure)
     * @param commitID   the identifier of the commit related to the build
     * @param logURL     the URL to the build log
     * @return           true if the email was sent successfully
     */
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

        performSend(email);

        System.out.println("HTML email sent!");

        return true;
    }

    /**
     * Attempts to send the specified email using the mailer
     * 
     * @param email the Email object to be sent
     * @return true if the email was sent successfully; false if an IllegalArgumentException occurred
     */
    protected boolean performSend(Email email) {
        try {
            this.mailer.sendMail(email);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
