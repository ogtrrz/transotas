import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './informacion.reducer';

export const InformacionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const informacionEntity = useAppSelector(state => state.informacion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="informacionDetailsHeading">
          <Translate contentKey="transotasApp.informacion.detail.title">Informacion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.id}</dd>
          <dt>
            <span id="comentarios">
              <Translate contentKey="transotasApp.informacion.comentarios">Comentarios</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.comentarios}</dd>
          <dt>
            <span id="vistas">
              <Translate contentKey="transotasApp.informacion.vistas">Vistas</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.vistas}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="transotasApp.informacion.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.rating}</dd>
          <dt>
            <span id="extra1">
              <Translate contentKey="transotasApp.informacion.extra1">Extra 1</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra1}</dd>
          <dt>
            <span id="extra2">
              <Translate contentKey="transotasApp.informacion.extra2">Extra 2</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra2}</dd>
          <dt>
            <span id="extra3">
              <Translate contentKey="transotasApp.informacion.extra3">Extra 3</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra3}</dd>
          <dt>
            <span id="extra4">
              <Translate contentKey="transotasApp.informacion.extra4">Extra 4</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra4}</dd>
          <dt>
            <span id="extra5">
              <Translate contentKey="transotasApp.informacion.extra5">Extra 5</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra5}</dd>
          <dt>
            <span id="extra6">
              <Translate contentKey="transotasApp.informacion.extra6">Extra 6</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra6}</dd>
          <dt>
            <span id="extra7">
              <Translate contentKey="transotasApp.informacion.extra7">Extra 7</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra7}</dd>
          <dt>
            <span id="extra8">
              <Translate contentKey="transotasApp.informacion.extra8">Extra 8</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra8}</dd>
          <dt>
            <span id="extra9">
              <Translate contentKey="transotasApp.informacion.extra9">Extra 9</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra9}</dd>
          <dt>
            <span id="extra10">
              <Translate contentKey="transotasApp.informacion.extra10">Extra 10</Translate>
            </span>
          </dt>
          <dd>{informacionEntity.extra10}</dd>
          <dt>
            <Translate contentKey="transotasApp.informacion.reportes">Reportes</Translate>
          </dt>
          <dd>{informacionEntity.reportes ? informacionEntity.reportes.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/informacion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/informacion/${informacionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InformacionDetail;
