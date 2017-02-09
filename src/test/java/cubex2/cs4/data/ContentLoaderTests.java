package cubex2.cs4.data;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cubex2.cs4.api.BlankContent;
import cubex2.cs4.api.Content;
import cubex2.cs4.api.ContentHelper;
import cubex2.cs4.api.InitPhase;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;

import static org.junit.Assert.*;

public class ContentLoaderTests
{
    private DeserializationRegistry deserializationRegistry = () -> Lists.newArrayList(Pair.of(InitPhase.class, new InitPhaseDeserializer()),
                                                                                       Pair.of(ContentLoader.class, ContentLoader.DESERIALIZER));

    @Test
    public void testLoadContent()
    {
        List<TestContent> list = loadContent("{\"list1\": [ {\"name\":\"a\"},{\"name\":\"b\"} ], \"list2\": [ {\"name\":\"c\"},{\"name\":\"d\"} ] }", TestContent.class);

        assertEquals(4, list.size());
        assertEquals("a", list.get(0).name);
        assertEquals("b", list.get(1).name);
        assertEquals("c", list.get(2).name);
        assertEquals("d", list.get(3).name);
    }

    @Test
    public void testLoadContent_empty()
    {
        List<TestContent> list = loadContent("{ }", TestContent.class);

        assertEquals(0, list.size());
    }

    @Test
    public void testLoadContent_emptyList()
    {
        List<TestContent> list = loadContent("{\"list\":[] }", TestContent.class);

        assertEquals(0, list.size());
    }

    @Test
    public void testLoadContent_emptyListAndNonEmptyList()
    {
        List<TestContent> list = loadContent("{\"list1\":[],\"list2\": [ {\"name\":\"c\"},{\"name\":\"d\"} ] }", TestContent.class);

        assertEquals(2, list.size());
        assertEquals("c", list.get(0).name);
        assertEquals("d", list.get(1).name);
    }

    @Test
    public void testLoadContent_objectInsteadOfList()
    {
        List<TestContent> list = loadContent("{\"list\": { \"name\":\"a\" } }", TestContent.class);

        assertEquals(1, list.size());
        assertEquals("a", list.get(0).name);
    }

    private <T extends Content> List<T> loadContent(String json, Class<T> contentClass)
    {
        return ContentLoader.loadContent(json, contentClass, deserializationRegistry);
    }

    @Test
    public void testPredicateDeserialization()
    {
        Gson gson = ContentLoader.registerAdapters(new GsonBuilder(), deserializationRegistry).create();
        ContentLoader loader = gson.fromJson("{\"singleValue\":\"theValue\",\"multiValue\":[\"value1\",\"value2\"]}", ContentLoader.class);

        List<String> singleValue = loader.predicateMap.get("singleValue");
        List<String> multiValue = loader.predicateMap.get("multiValue");
        assertEquals(2, loader.predicateMap.size());
        assertEquals(1, singleValue.size());
        assertEquals("theValue", singleValue.get(0));
        assertEquals(2, multiValue.size());
        assertEquals("value1", multiValue.get(0));
        assertEquals("value2", multiValue.get(1));
    }

    @Test
    public void testShouldInit()
    {
        ContentLoader loader = new ContentLoader();
        loader.initPhase = InitPhase.INIT;

        assertFalse(loader.shouldInit(InitPhase.PRE_INIT));
        assertTrue(loader.shouldInit(InitPhase.INIT));
        assertFalse(loader.shouldInit(InitPhase.POST_INIT));
    }

    @Test
    public void testShouldInit_nullPhase()
    {
        ContentLoader loader = new ContentLoader();
        loader.initPhase = null;

        for (InitPhase phase : InitPhase.values())
        {
            assertTrue(loader.shouldInit(phase));
        }
    }

    @Test
    public void testShouldInit_nullType()
    {
        ContentLoader loader = new ContentLoader();
        loader.type = null;
        loader.file = "";

        for (InitPhase phase : InitPhase.values())
        {
            assertFalse(loader.shouldInit(phase));
        }
    }

    @Test
    public void testShouldInit_nullFile()
    {
        ContentLoader loader = new ContentLoader();
        loader.type = "";
        loader.file = null;

        for (InitPhase phase : InitPhase.values())
        {
            assertFalse(loader.shouldInit(phase));
        }
    }

    @Test
    public void testCheckPredicates_falsePredicate()
    {
        ContentLoader loader = new ContentLoader();
        loader.predicateMap.put("thePredicate", Lists.newArrayList("theValue"));

        assertFalse(loader.checkPredicates(name -> (arguments -> false)));
    }

    @Test
    public void testCheckPredicates_truePredicate()
    {
        ContentLoader loader = new ContentLoader();
        loader.predicateMap.put("thePredicate", Lists.newArrayList("theValue"));

        assertTrue(loader.checkPredicates(name -> (arguments -> true)));
    }

    @Test
    public void testCheckPredicates_trueAndFalsePredicates()
    {
        ContentLoader loader = new ContentLoader();
        loader.predicateMap.put("true", Lists.newArrayList("true"));
        loader.predicateMap.put("false", Lists.newArrayList("false"));

        assertFalse(loader.checkPredicates(name -> (arguments -> arguments.contains("true"))));
    }

    private static List<TestContent> initializedContent;

    @Test
    public void testInit()
    {
        initializedContent = Lists.newArrayList();

        ContentLoader loader = new ContentLoader();
        loader.init(InitPhase.INIT, new TestContentHelper());

        assertEquals(2, initializedContent.size());
    }

    @Test
    public void testInit_loaderLoadsLoader()
    {
        initializedContent = Lists.newArrayList();

        ContentLoader loader = new ContentLoader();
        loader.file = "loader";
        loader.type = "contentLoader";
        loader.init(InitPhase.INIT, new TestContentHelper());

        assertEquals(2, initializedContent.size());
    }

    private static class TestContentHelper implements ContentHelper
    {

        @Nullable
        @Override
        public String readJson(String path)
        {
            if (path.equals("loader"))
                return "{\"list1\": [ {\"type\":\"test\", \"file\":\"someFile.json\"} ] }";
            else
                return "{\"list1\": [ {\"name\":\"a\"},{\"name\":\"b\"} ] }";
        }

        @Nullable
        @Override
        public Class<? extends Content> getContentClass(String typeName)
        {
            if (typeName.equals("contentLoader"))
                return ContentLoader.class;
            return TestContent.class;
        }
    }

    public static class TestContent extends BlankContent
    {
        public String name;

        @Override
        public void init(InitPhase phase, ContentHelper helper)
        {
            initializedContent.add(this);
        }
    }
}
