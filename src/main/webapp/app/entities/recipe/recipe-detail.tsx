import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './recipe.reducer';

export const RecipeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const recipeEntity = useAppSelector(state => state.recipe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recipeDetailsHeading">Recipe</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{recipeEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{recipeEntity.title}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{recipeEntity.description}</dd>
          <dt>
            <span id="cuisine">Cuisine</span>
          </dt>
          <dd>{recipeEntity.cuisine}</dd>
          <dt>
            <span id="difficultyLevel">Difficulty Level</span>
          </dt>
          <dd>{recipeEntity.difficultyLevel}</dd>
          <dt>
            <span id="preparationTime">Preparation Time</span>
          </dt>
          <dd>{recipeEntity.preparationTime}</dd>
          <dt>
            <span id="cookingTime">Cooking Time</span>
          </dt>
          <dd>{recipeEntity.cookingTime}</dd>
          <dt>Author</dt>
          <dd>{recipeEntity.author ? recipeEntity.author.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/recipe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/recipe/${recipeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecipeDetail;
