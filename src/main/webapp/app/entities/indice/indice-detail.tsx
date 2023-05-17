import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './indice.reducer';

export const IndiceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const indiceEntity = useAppSelector(state => state.indice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="indiceDetailsHeading">
          <Translate contentKey="transotasApp.indice.detail.title">Indice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.id}</dd>
          <dt>
            <span id="img">
              <Translate contentKey="transotasApp.indice.img">Img</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.img}</dd>
          <dt>
            <span id="titulo">
              <Translate contentKey="transotasApp.indice.titulo">Titulo</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.titulo}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="transotasApp.indice.url">Url</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.url}</dd>
          <dt>
            <span id="autor">
              <Translate contentKey="transotasApp.indice.autor">Autor</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.autor}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="transotasApp.indice.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.fecha ? <TextFormat value={indiceEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="ciudad">
              <Translate contentKey="transotasApp.indice.ciudad">Ciudad</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.ciudad}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="transotasApp.indice.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.estado}</dd>
          <dt>
            <span id="pais">
              <Translate contentKey="transotasApp.indice.pais">Pais</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.pais}</dd>
          <dt>
            <span id="comentarios">
              <Translate contentKey="transotasApp.indice.comentarios">Comentarios</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.comentarios}</dd>
          <dt>
            <span id="vistas">
              <Translate contentKey="transotasApp.indice.vistas">Vistas</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.vistas}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="transotasApp.indice.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.rating}</dd>
          <dt>
            <span id="extra1">
              <Translate contentKey="transotasApp.indice.extra1">Extra 1</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra1}</dd>
          <dt>
            <span id="extra2">
              <Translate contentKey="transotasApp.indice.extra2">Extra 2</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra2}</dd>
          <dt>
            <span id="extra3">
              <Translate contentKey="transotasApp.indice.extra3">Extra 3</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra3}</dd>
          <dt>
            <span id="extra4">
              <Translate contentKey="transotasApp.indice.extra4">Extra 4</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra4}</dd>
          <dt>
            <span id="extra5">
              <Translate contentKey="transotasApp.indice.extra5">Extra 5</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra5}</dd>
          <dt>
            <span id="extra6">
              <Translate contentKey="transotasApp.indice.extra6">Extra 6</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra6}</dd>
          <dt>
            <span id="extra7">
              <Translate contentKey="transotasApp.indice.extra7">Extra 7</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra7}</dd>
          <dt>
            <span id="extra8">
              <Translate contentKey="transotasApp.indice.extra8">Extra 8</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra8}</dd>
          <dt>
            <span id="extra9">
              <Translate contentKey="transotasApp.indice.extra9">Extra 9</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra9}</dd>
          <dt>
            <span id="extra10">
              <Translate contentKey="transotasApp.indice.extra10">Extra 10</Translate>
            </span>
          </dt>
          <dd>{indiceEntity.extra10}</dd>
        </dl>
        <Button tag={Link} to="/indice" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/indice/${indiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IndiceDetail;
