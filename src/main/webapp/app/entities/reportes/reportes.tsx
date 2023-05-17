import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReportes } from 'app/shared/model/reportes.model';
import { searchEntities, getEntities } from './reportes.reducer';

export const Reportes = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const reportesList = useAppSelector(state => state.reportes.entities);
  const loading = useAppSelector(state => state.reportes.loading);
  const totalItems = useAppSelector(state => state.reportes.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="reportes-heading" data-cy="ReportesHeading">
        <Translate contentKey="transotasApp.reportes.home.title">Reportes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="transotasApp.reportes.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/reportes/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="transotasApp.reportes.home.createLabel">Create new Reportes</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('transotasApp.reportes.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {reportesList && reportesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="transotasApp.reportes.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('titulo')}>
                  <Translate contentKey="transotasApp.reportes.titulo">Titulo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('caso')}>
                  <Translate contentKey="transotasApp.reportes.caso">Caso</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('img')}>
                  <Translate contentKey="transotasApp.reportes.img">Img</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('autor')}>
                  <Translate contentKey="transotasApp.reportes.autor">Autor</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tituloix')}>
                  <Translate contentKey="transotasApp.reportes.tituloix">Tituloix</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('autorix')}>
                  <Translate contentKey="transotasApp.reportes.autorix">Autorix</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fechaix')}>
                  <Translate contentKey="transotasApp.reportes.fechaix">Fechaix</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('imgix')}>
                  <Translate contentKey="transotasApp.reportes.imgix">Imgix</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ciudad')}>
                  <Translate contentKey="transotasApp.reportes.ciudad">Ciudad</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('estado')}>
                  <Translate contentKey="transotasApp.reportes.estado">Estado</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pais')}>
                  <Translate contentKey="transotasApp.reportes.pais">Pais</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra1')}>
                  <Translate contentKey="transotasApp.reportes.extra1">Extra 1</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra2')}>
                  <Translate contentKey="transotasApp.reportes.extra2">Extra 2</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra3')}>
                  <Translate contentKey="transotasApp.reportes.extra3">Extra 3</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra4')}>
                  <Translate contentKey="transotasApp.reportes.extra4">Extra 4</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra5')}>
                  <Translate contentKey="transotasApp.reportes.extra5">Extra 5</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra6')}>
                  <Translate contentKey="transotasApp.reportes.extra6">Extra 6</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra7')}>
                  <Translate contentKey="transotasApp.reportes.extra7">Extra 7</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra8')}>
                  <Translate contentKey="transotasApp.reportes.extra8">Extra 8</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra9')}>
                  <Translate contentKey="transotasApp.reportes.extra9">Extra 9</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('extra10')}>
                  <Translate contentKey="transotasApp.reportes.extra10">Extra 10</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="transotasApp.reportes.informacion">Informacion</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="transotasApp.reportes.caso">Caso</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {reportesList.map((reportes, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/reportes/${reportes.id}`} color="link" size="sm">
                      {reportes.id}
                    </Button>
                  </td>
                  <td>{reportes.titulo}</td>
                  <td>{reportes.caso}</td>
                  <td>{reportes.img}</td>
                  <td>{reportes.autor}</td>
                  <td>{reportes.tituloix}</td>
                  <td>{reportes.autorix}</td>
                  <td>{reportes.fechaix ? <TextFormat type="date" value={reportes.fechaix} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{reportes.imgix}</td>
                  <td>{reportes.ciudad}</td>
                  <td>{reportes.estado}</td>
                  <td>{reportes.pais}</td>
                  <td>{reportes.extra1}</td>
                  <td>{reportes.extra2}</td>
                  <td>{reportes.extra3}</td>
                  <td>{reportes.extra4}</td>
                  <td>{reportes.extra5}</td>
                  <td>{reportes.extra6}</td>
                  <td>{reportes.extra7}</td>
                  <td>{reportes.extra8}</td>
                  <td>{reportes.extra9}</td>
                  <td>{reportes.extra10}</td>
                  <td>
                    {reportes.informacion ? <Link to={`/informacion/${reportes.informacion.id}`}>{reportes.informacion.id}</Link> : ''}
                  </td>
                  <td>{reportes.caso ? <Link to={`/caso/${reportes.caso.id}`}>{reportes.caso.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/reportes/${reportes.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/reportes/${reportes.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/reportes/${reportes.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="transotasApp.reportes.home.notFound">No Reportes found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={reportesList && reportesList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Reportes;
