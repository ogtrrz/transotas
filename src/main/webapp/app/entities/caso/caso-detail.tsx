import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './caso.reducer';

export const CasoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const casoEntity = useAppSelector(state => state.caso.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="casoDetailsHeading">
          <Translate contentKey="transotasApp.caso.detail.title">Caso</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{casoEntity.id}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="transotasApp.caso.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{casoEntity.descripcion}</dd>
          <dt>
            <span id="extra1">
              <Translate contentKey="transotasApp.caso.extra1">Extra 1</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra1}</dd>
          <dt>
            <span id="extra2">
              <Translate contentKey="transotasApp.caso.extra2">Extra 2</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra2}</dd>
          <dt>
            <span id="extra3">
              <Translate contentKey="transotasApp.caso.extra3">Extra 3</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra3}</dd>
          <dt>
            <span id="extra4">
              <Translate contentKey="transotasApp.caso.extra4">Extra 4</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra4}</dd>
          <dt>
            <span id="extra5">
              <Translate contentKey="transotasApp.caso.extra5">Extra 5</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra5}</dd>
          <dt>
            <span id="extra6">
              <Translate contentKey="transotasApp.caso.extra6">Extra 6</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra6}</dd>
          <dt>
            <span id="extra7">
              <Translate contentKey="transotasApp.caso.extra7">Extra 7</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra7}</dd>
          <dt>
            <span id="extra8">
              <Translate contentKey="transotasApp.caso.extra8">Extra 8</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra8}</dd>
          <dt>
            <span id="extra9">
              <Translate contentKey="transotasApp.caso.extra9">Extra 9</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra9}</dd>
          <dt>
            <span id="extra10">
              <Translate contentKey="transotasApp.caso.extra10">Extra 10</Translate>
            </span>
          </dt>
          <dd>{casoEntity.extra10}</dd>
        </dl>
        <Button tag={Link} to="/caso" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/caso/${casoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CasoDetail;
