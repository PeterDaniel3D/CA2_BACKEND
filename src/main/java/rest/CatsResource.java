package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import utils.EMF_Creator;
import utils.HttpUtils;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Path("cat")
public class CatsResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    Gson GSON = new Gson();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    //@RolesAllowed("user")
    public String getCatPicture() throws IOException {
        return GSON.toJson(HttpUtils.fetchData("https://api.thecatapi.com/v1/images/search"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voice")
    @RolesAllowed("user")
    public String getVoiceRSS() throws IOException {
        JsonObject catFacts = HttpUtils.fetchJson("https://catfact.ninja/fact");
//                HttpUtils.fetchData("https://catfact.ninja/fact");
        String fact = String.valueOf(catFacts.get("fact"));
        fact = fact.replaceAll("\"", ""); // Remove quotations around json element

        List<String> bonusComment = new ArrayList<>();
        bonusComment.add("Jeez! I don't believe it. These facts are trash!");
        bonusComment.add("To be honest, I love dogs more than cats!");
        bonusComment.add("This fact is just pure garbage.");
        bonusComment.add("One cat, one hat. Chuck Norris, no cat. Ha ha ha ha.");

        Random rand = new Random();
        int randInt = rand.nextInt(bonusComment.size());

        String json = "{\"url\":\"http://api.voicerss.org/?key=4a3189fb0bf34912aec4aea4873d90b0&hl=en-gb&v=Harry&c=MP3&f=16khz_16bit_stereo&src=" + fact + " " + bonusComment.get(randInt) + ".\", \"fact\":\"" + fact + "\"}";
        return GSON.toJson(json);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getCatPictureForAdmin() throws IOException {
        //DOGS IN A CAT ENDPOINT??? WHAT KIND DARK MAGIC IS THIS??
        return GSON.toJson(HttpUtils.fetchData("https://dog-api.kinduff.com/api/facts"));
    }
}
