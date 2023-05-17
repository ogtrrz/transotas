import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './caso-text.reducer';

export const CasoTextDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const casoTextEntity = useAppSelector(state => state.casoText.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="casoTextDetailsHeading">
          <Translate contentKey="transotasApp.casoText.detail.title">CasoText</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.id}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="transotasApp.casoText.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.descripcion}</dd>
          <dt>
            <span id="extra1">
              <Translate contentKey="transotasApp.casoText.extra1">Extra 1</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra1}</dd>
          <dt>
            <span id="extra2">
              <Translate contentKey="transotasApp.casoText.extra2">Extra 2</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra2}</dd>
          <dt>
            <span id="extra3">
              <Translate contentKey="transotasApp.casoText.extra3">Extra 3</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra3}</dd>
          <dt>
            <span id="extra4">
              <Translate contentKey="transotasApp.casoText.extra4">Extra 4</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra4}</dd>
          <dt>
            <span id="extra5">
              <Translate contentKey="transotasApp.casoText.extra5">Extra 5</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra5}</dd>
          <dt>
            <span id="extra6">
              <Translate contentKey="transotasApp.casoText.extra6">Extra 6</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra6}</dd>
          <dt>
            <span id="extra7">
              <Translate contentKey="transotasApp.casoText.extra7">Extra 7</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra7}</dd>
          <dt>
            <span id="extra8">
              <Translate contentKey="transotasApp.casoText.extra8">Extra 8</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra8}</dd>
          <dt>
            <span id="extra9">
              <Translate contentKey="transotasApp.casoText.extra9">Extra 9</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra9}</dd>
          <dt>
            <span id="extra10">
              <Translate contentKey="transotasApp.casoText.extra10">Extra 10</Translate>
            </span>
          </dt>
          <dd>{casoTextEntity.extra10}</dd>
        </dl>
        <Button tag={Link} to="/caso-text" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/caso-text/${casoTextEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CasoTextDetail;
