package hu.bozgab.discord.events.music;

public class QueuedTrack {
    private String uri,title,author;

    public QueuedTrack(String uri, String title, String author) {
        this.uri = uri;
        this.title = title;
        this.author = author;
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
