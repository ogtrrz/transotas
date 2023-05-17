import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './indice-original.reducer';

export const IndiceOriginalDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const indiceOriginalEntity = useAppSelector(state => state.indiceOriginal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="indiceOriginalDetailsHeading">
          <Translate contentKey="transotasApp.indiceOriginal.detail.title">IndiceOriginal</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.id}</dd>
          <dt>
            <span id="img">
              <Translate contentKey="transotasApp.indiceOriginal.img">Img</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.img}</dd>
          <dt>
            <span id="titulo">
              <Translate contentKey="transotasApp.indiceOriginal.titulo">Titulo</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.titulo}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="transotasApp.indiceOriginal.url">Url</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.url}</dd>
          <dt>
            <span id="autor">
              <Translate contentKey="transotasApp.indiceOriginal.autor">Autor</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.autor}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="transotasApp.indiceOriginal.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>
            {indiceOriginalEntity.fecha ? <TextFormat value={indiceOriginalEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="ciudad">
              <Translate contentKey="transotasApp.indiceOriginal.ciudad">Ciudad</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.ciudad}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="transotasApp.indiceOriginal.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.estado}</dd>
          <dt>
            <span id="pais">
              <Translate contentKey="transotasApp.indiceOriginal.pais">Pais</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.pais}</dd>
          <dt>
            <span id="comentarios">
              <Translate contentKey="transotasApp.indiceOriginal.comentarios">Comentarios</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.comentarios}</dd>
          <dt>
            <span id="vistas">
              <Translate contentKey="transotasApp.indiceOriginal.vistas">Vistas</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.vistas}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="transotasApp.indiceOriginal.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{indiceOriginalEntity.rating}</dd>
        </dl>
        <Button tag={Link} to="/indice-original" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/indice-original/${indiceOriginalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IndiceOriginalDetail;
