package org.pinkcrazyunicorn.quickie.application.profile;

import static org.assertj.core.api.Assertions.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.pinkcrazyunicorn.quickie.domain.Food;
import org.pinkcrazyunicorn.quickie.domain.profile.Opinion;
import org.pinkcrazyunicorn.quickie.domain.profile.Profile;
import org.pinkcrazyunicorn.quickie.domain.profile.ProfileRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ProfileServiceTest {

    @Test
    public void shouldAddProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        repository.add("Default");

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        codeUnderTest.add("Default");

        EasyMock.verify(repository);
    }

    @Test
    public void shouldRemoveProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        repository.remove("Default");

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        codeUnderTest.remove("Default");

        EasyMock.verify(repository);
    }

    @Test
    public void shouldGetProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Profile profile = EasyMock.createMock(Profile.class);
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.of(profile));

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        Optional<Profile> actual = codeUnderTest.getBy("Default");

        EasyMock.verify(repository);
        assertThat(actual).isPresent().hasValue(profile);
    }

    @Test
    public void shouldNotGetInvalidProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.empty());

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        Optional<Profile> actual = codeUnderTest.getBy("Default");

        EasyMock.verify(repository);
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldGetAllProfiles() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Profile profile1 = EasyMock.createMock(Profile.class);
        Profile profile2 = EasyMock.createMock(Profile.class);
        Profile profile3 = EasyMock.createMock(Profile.class);
        EasyMock.expect(repository.getAll()).andReturn(List.of(profile1, profile2, profile3));

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        Collection<Profile> actual = codeUnderTest.getAll();

        EasyMock.verify(repository);
        assertThat(actual)
                .hasSize(3)
                .contains(profile1)
                .contains(profile2)
                .contains(profile3);
    }

    @Test
    public void shouldGetNoProfilesAsAll() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        EasyMock.expect(repository.getAll()).andReturn(List.of());

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        Collection<Profile> actual = codeUnderTest.getAll();

        EasyMock.verify(repository);
        assertThat(actual)
                .hasSize(0);
    }

    @Test
    public void shouldSetOpinionAboutFood() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Profile profile = EasyMock.createMock(Profile.class);
        Food food = new Food("Food");
        Opinion opinion = EasyMock.createMock(Opinion.class);
        profile.setOpinionAbout(food, opinion);
        repository.update(profile);
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.of(profile));

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        codeUnderTest.setOpinionAbout("Default", food, opinion);

        EasyMock.verify(repository);
    }

    @Test
    public void shouldErrWhenSettingOpinionForInvalidProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Food food = new Food("Food");
        Opinion opinion = EasyMock.createMock(Opinion.class);
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.empty());

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        assertThatThrownBy(() -> {
            codeUnderTest.setOpinionAbout("Default", food, opinion);
        }).isInstanceOf(IllegalArgumentException.class);

        EasyMock.verify(repository);
    }

    @Test
    public void shouldMarkFoodAsAvailable() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Profile profile = EasyMock.createMock(Profile.class);
        Food food = new Food("Food");
        profile.addToAvailable(food);
        repository.update(profile);
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.of(profile));

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        codeUnderTest.addToAvailable("Default", food);

        EasyMock.verify(repository);
    }

    @Test
    public void shouldErrWhenMarkingFoodAsAvailableForInvalidProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Food food = new Food("Food");
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.empty());

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        assertThatThrownBy(() -> {
            codeUnderTest.addToAvailable("Default", food);
        }).isInstanceOf(IllegalArgumentException.class);

        EasyMock.verify(repository);
    }

    @Test
    public void shouldRemoveOpinionAbout() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Profile profile = EasyMock.createMock(Profile.class);
        Food food = new Food("Food");
        profile.removeOpinionAbout(food);
        repository.update(profile);
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.of(profile));

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        codeUnderTest.removeOpinionAbout("Default", food);

        EasyMock.verify(repository);
    }

    @Test
    public void shouldErrWhenRemovingOpinionForInvalidProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Food food = new Food("Food");
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.empty());

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        assertThatThrownBy(() -> {
            codeUnderTest.removeOpinionAbout("Default", food);
        }).isInstanceOf(IllegalArgumentException.class);

        EasyMock.verify(repository);
    }

    @Test
    public void shouldMarkFoodAsUnavailable() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Profile profile = EasyMock.createMock(Profile.class);
        Food food = new Food("Food");
        profile.markUnavailable(food);
        repository.update(profile);
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.of(profile));

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        codeUnderTest.markUnavailable("Default", food);

        EasyMock.verify(repository);
    }

    @Test
    public void shouldErrWhenMarkingFoodAsUnavailableForInvalidProfile() {
        ProfileRepository repository = EasyMock.createMock(ProfileRepository.class);
        Food food = new Food("Food");
        EasyMock.expect(repository.getBy("Default")).andReturn(Optional.empty());

        EasyMock.replay(repository);
        ProfileService codeUnderTest = new ProfileService(repository);

        assertThatThrownBy(() -> {
            codeUnderTest.markUnavailable("Default", food);
        }).isInstanceOf(IllegalArgumentException.class);

        EasyMock.verify(repository);
    }
}
