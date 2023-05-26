package sk.stuba.fei.uim.vsa.pr2.rest;

public interface ResponseFactory<R, T ,requestBody> {

    T transformToDto(R entity);

    R transformToEntity(requestBody requestBody);


}
