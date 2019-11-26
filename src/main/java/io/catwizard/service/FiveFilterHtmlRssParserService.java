package io.catwizard.service;


import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import de.l3s.boilerpipe.BoilerpipeProcessingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@Service
@Transactional
public class FiveFilterHtmlRssParserService {
    private final Logger log = LoggerFactory.getLogger(FiveFilterHtmlRssParserService.class);

    public static String LETS_BLOCK_ADS = "<p><strong><a href=\"https://blockads.fivefilters.org\">Let's block ads!</a></strong> <a href=\"https://blockads.fivefilters.org/acceptable.html\">(Why?)</a></p>";


    public String getArticleFullText(String stringUrl) throws IOException, FeedException, NullPointerException {

        String url = "http://ftr.fivefilters.org/makefulltextfeed.php?url="+ URLEncoder.encode(stringUrl.toString(), "ISO-8859-1")+"&max=3";

        // Esta es una mejor forma supuestamente de la que esta implementada https://github.com/rometools/rome/issues/276
        log.debug("Parsing "+url);
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
        // log.debug(feed.getTitle());
        if ((feed == null ) || (feed.getEntries() == null ) ||  (feed.getEntries().size() == 0)){

            return ArticleService.UNABLE_TO_RETRIEVE_FULL_TEXT_CONTENT;

        }else {


            SyndEntryImpl entry1 = (SyndEntryImpl) (feed.getEntries().get(0));

            SyndContent syndContent = entry1.getDescription();

            return syndContent.getValue().replace(LETS_BLOCK_ADS, "");
        }
    }

    public String getArticleFullTextBoiler(String urlString) throws BoilerpipeProcessingException, MalformedURLException, UnsupportedEncodingException {

        // https://boilerpipe-web.appspot.com/extract?url=https%3A%2F%2Flittlecoffeefox.com%2Fmorning-pages-changed-life%2F&extractor=ArticleExtractor&output=htmlFragment&extractImages=&token=
        // URL url = new URL(urlString);

        String url = "https://boilerpipe-web.appspot.com/extract?url="+ URLEncoder.encode(urlString.toString(), "ISO-8859-1")+"&extractor=ArticleExtractor&output=htmlFragment&extractImages=&token=";

        Document doc = null;
        try {
            doc = Jsoup.connect(urlString).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = doc.html();

        ///////  This code actually does returns plain text. Because que rely on html now, it might not be that helpfull, but it can be in the future when we have NLP that

        // URL url2 = new URL(urlString); // NOTE: Use ArticleExtractor unless DefaultExtractor gives better results for you String text = ArticleExtractor.INSTANCE.getText(url);

        //   String result2 = ArticleExtractor.INSTANCE.getText(doc.html());

        //////

        return result;
    }
}
