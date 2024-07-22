package com.keyin.hello;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GreetingService {
    private final GreetingRepository greetingRepository;

    private final LanguageRepository languageRepository;

    public GreetingService(GreetingRepository greetingRepository, LanguageRepository languageRepository) {
        this.greetingRepository = greetingRepository;
        this.languageRepository = languageRepository;
    }

    public Greeting getGreeting(long index) {
        Optional<Greeting> result = greetingRepository.findById(index);

        return result.orElse(null);

    }

    public Greeting createGreeting(Greeting newGreeting) {
        newGreeting.setId(0);

        if (newGreeting.getLanguages() == null) {
            Language english = languageRepository.findByName("English");

            if (english == null) {
                english = new Language();
                languageRepository.save(english);
            }

            ArrayList<Language> languageArrayList = new ArrayList<>();
            languageArrayList.add(english);

            newGreeting.setLanguages(languageArrayList);
        } else {
            ArrayList<Language> languageArrayList = new ArrayList<>();
            for (Language language : newGreeting.getLanguages()) {
                Language langInDB = languageRepository.findByName(language.getName());

                if (langInDB == null) {
                    language = languageRepository.save(language);
                    languageArrayList.add(language);
                } else {
                    language.setId(0);
                    language = langInDB;
                    languageArrayList.add(language);
                }

            }
            newGreeting.setLanguages(languageArrayList);
        }

        return greetingRepository.save(newGreeting);
    }

    public List<Greeting> getAllGreetings() {
        return (List<Greeting>) greetingRepository.findAll();
    }

    public Greeting updateGreeting(Integer index, Greeting updatedGreeting) {
        Greeting greetingToUpdate = getGreeting(index);

        greetingToUpdate.setName(updatedGreeting.getName());
        greetingToUpdate.setGreeting(updatedGreeting.getGreeting());

        ArrayList<Language> languageArrayList = new ArrayList<>();
        for (Language language : updatedGreeting.getLanguages()) {
            Language langInDB = languageRepository.findByName(language.getName());

            if (langInDB == null) {
                language.setId(0);
                language = languageRepository.save(language);
                languageArrayList.add(language);
            } else {
                language = langInDB;
                languageArrayList.add(language);
            }
        }

        greetingToUpdate.setLanguages(languageArrayList);

        return greetingRepository.save(greetingToUpdate);
    }

    public void deleteGreeting(long index) {
        greetingRepository.delete(getGreeting(index));
    }

    public List<Greeting> findGreetingsByNameAndGreeting(String name, String greetingName) {
        return greetingRepository.findByNameAndGreeting(name, greetingName);
    }
}