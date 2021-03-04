package post.that.utils.Translation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class Internationalization
{
	private static Internationalization instance;

	private static final String DEFAULT_TRANSLATION_FOLDER = "locales";
	private static final TranslationFileFilter filter = new TranslationFileFilter();

	private Map<String, Map<String, String>> translation;
	private String currentLanguage;
	private String fallbackLanguage;

	private Internationalization() throws IOException
	{
		this.translation = new HashMap<String, Map<String, String>>();
		this.setFallback(Locale.ENGLISH.getLanguage());
		this.setLangague(Locale.getDefault().getLanguage());
		this.loadTranslation(Internationalization.DEFAULT_TRANSLATION_FOLDER);
	}

	private void loadTranslation(String translationFolderPath) throws IOException
	{
		File translationFolder = new File(translationFolderPath);
		if(translationFolder.isDirectory())
		{
			this.parseTranslationFolderContent(translationFolder);
		}
		else
		{
			throw new IOException("The translation folder path (" + translationFolderPath + ") is not a directory");
		}
	}

	public void setLangague(String language)
	{
		this.currentLanguage = language;
	}

	public void setFallback(String language)
	{
		this.fallbackLanguage = language;
	}

	public String translate(String key)
	{
		Map<String, String> translations = getTranslations();

		if(translations.containsKey(key))
		{
			return translations.get(key);
		}
		else
		{
			return key;
		}
	}

	private Map<String, String> getTranslations()
	{
		Map<String, String> translationForCurrentLanguage = this.translation.get(this.currentLanguage);

		if(translationForCurrentLanguage == null)
		{
			translationForCurrentLanguage = this.translation.get(this.fallbackLanguage);
		}
		
		if(translationForCurrentLanguage == null)
			return Map.of();
		else
			return translationForCurrentLanguage;
	}

	private void parseTranslationFolderContent(File translationFolder) throws IOException
	{
		for(File file : translationFolder.listFiles(Internationalization.filter))
		{
			if(file.isFile())
			{
				this.parseTranslationFile(file);
			}
		}
	}

	private void parseTranslationFile(File file) throws IOException
	{
		TranslationFile translationFile = new TranslationFile(file);
		this.translation.put(translationFile.getLanguage(), translationFile.asMap());
	}

	public static Internationalization getInstance()
	{
		if(Internationalization.instance == null)
		{
			try
			{
				Internationalization.instance = new Internationalization();
			}
			catch(IOException e)
			{
			}
		}

		return Internationalization.instance;
	}

	public static String get(String key)
	{
		return Internationalization.getInstance().translate(key);
	}

	public static class TranslationFileFilter implements FilenameFilter
	{
		private static final String TRANSLATION_FILE_EXTENSION = ".lang";

		@Override
		public boolean accept(File parent, String name)
		{
			return name.endsWith(TranslationFileFilter.TRANSLATION_FILE_EXTENSION);
		}

	}

	public static class TranslationFile
	{
		private static final String KEY_VALUE_SEPARATOR = "=";

		private File file;
		private Map<String, String> translation;

		public TranslationFile(String path) throws IOException
		{
			this(new File(path));
		}

		public TranslationFile(File file) throws IOException
		{
			this.file = file;
			this.translation = new HashMap<String, String>();
			this.parse();
		}

		public String getLanguage()
		{
			return this.file.getName().split("\\.")[0];
		}

		public Map<String, String> asMap()
		{
			return this.translation;
		}

		private void parse() throws IOException
		{
			List<String> lines = Files.readAllLines(Paths.get(this.file.getPath()));
			for(String line : lines)
			{
				Entry<String, String> entry = parseLine(line);
				this.translation.put(entry.getKey(), entry.getValue());
			}
		}

		private Entry<String, String> parseLine(String line) throws IOException
		{
			String[] keyValue = line.split(TranslationFile.KEY_VALUE_SEPARATOR);
			
			if(keyValue.length == 2)
			{
				return Map.entry(keyValue[0], keyValue[1]);
			}
			else
			{
				throw new IOException("Incorrect line, cannot parse : " + line);
			}
		}
	}
}
