import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ingredient.reducer';

export const IngredientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ingredientEntity = useAppSelector(state => state.ingredient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingredientDetailsHeading">Ingredient</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{ingredientEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{ingredientEntity.name}</dd>
          <dt>
            <span id="quantity">Quantity</span>
          </dt>
          <dd>{ingredientEntity.quantity}</dd>
          <dt>
            <span id="unit">Unit</span>
          </dt>
          <dd>{ingredientEntity.unit}</dd>
          <dt>Recipe</dt>
          <dd>{ingredientEntity.recipe ? ingredientEntity.recipe.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ingredient" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ingredient/${ingredientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default IngredientDetail;
