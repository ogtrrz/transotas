import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './comentarios.reducer';

export const ComentariosDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const comentariosEntity = useAppSelector(state => state.comentarios.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="comentariosDetailsHeading">
          <Translate contentKey="transotasApp.comentarios.detail.title">Comentarios</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.id}</dd>
          <dt>
            <span id="usuario">
              <Translate contentKey="transotasApp.comentarios.usuario">Usuario</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.usuario}</dd>
          <dt>
            <span id="comentario">
              <Translate contentKey="transotasApp.comentarios.comentario">Comentario</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.comentario}</dd>
          <dt>
            <span id="extra1">
              <Translate contentKey="transotasApp.comentarios.extra1">Extra 1</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra1}</dd>
          <dt>
            <span id="extra2">
              <Translate contentKey="transotasApp.comentarios.extra2">Extra 2</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra2}</dd>
          <dt>
            <span id="extra3">
              <Translate contentKey="transotasApp.comentarios.extra3">Extra 3</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra3}</dd>
          <dt>
            <span id="extra4">
              <Translate contentKey="transotasApp.comentarios.extra4">Extra 4</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra4}</dd>
          <dt>
            <span id="extra5">
              <Translate contentKey="transotasApp.comentarios.extra5">Extra 5</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra5}</dd>
          <dt>
            <span id="extra6">
              <Translate contentKey="transotasApp.comentarios.extra6">Extra 6</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra6}</dd>
          <dt>
            <span id="extra7">
              <Translate contentKey="transotasApp.comentarios.extra7">Extra 7</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra7}</dd>
          <dt>
            <span id="extra8">
              <Translate contentKey="transotasApp.comentarios.extra8">Extra 8</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra8}</dd>
          <dt>
            <span id="extra9">
              <Translate contentKey="transotasApp.comentarios.extra9">Extra 9</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra9}</dd>
          <dt>
            <span id="extra10">
              <Translate contentKey="transotasApp.comentarios.extra10">Extra 10</Translate>
            </span>
          </dt>
          <dd>{comentariosEntity.extra10}</dd>
          <dt>
            <Translate contentKey="transotasApp.comentarios.reportes">Reportes</Translate>
          </dt>
          <dd>{comentariosEntity.reportes ? comentariosEntity.reportes.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/comentarios" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comentarios/${comentariosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComentariosDetail;
