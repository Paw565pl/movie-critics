package dev.paw565pl.movie_critics.data;

public interface TestDataFactory<T> {

    T createOne();

    Iterable<T> createMany(int count);

    void clear();
}
