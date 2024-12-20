import React, { useEffect, useState } from 'react'
import './Mainpage.css';
import MealCard from '../MealCard/MealCard';

const Mainpage = () => {
  const [data, setData] = useState();
  const [input, setInput] = useState("");
  const[msg, setmsg] = useState("");
  const[categories, setCatgories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch('https://www.themealdb.com/api/json/v1/1/categories.php');
        const jsonData = await response.json();
        if (jsonData.categories) {
          setCatgories(jsonData.categories); // Store categories
        } else {
          console.error('No categories found');
        }
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    fetchCategories();
  }, []);


  const searchValue = (event) => {
    setInput(event.target.value)

  }
  const myFun = async () => {
    if(input == ""){
      setmsg("Please enter something")

    }
    else{
      const info = await fetch(`https://www.themealdb.com/api/json/v1/1/search.php?s=${input}`);
    const jsonData = await info.json()
    // console.log(jsonData.meals);
    setData(jsonData.meals)
    setmsg("")

    }
    
  }
  const handleCategoryClick = async(category) =>{
    setmsg(`Fetching meals in category: ${category}...`);
    try{
    const output = await fetch(`https://www.themealdb.com/api/json/v1/1/filter.php?c=${category}`);
    const jsonData = output.json();
    setData = (jsonData.meals);
    setmsg("");

  }catch(error){
    console.log('Error fetching meals for Category:', error);
    setmsg("Error fetching meals. Please try again");
  }

  };

  return (
    <>
    <div className = 'container' >
        <div className='searchBar'>
            <input type='text' placeholder='What do yo want to eat today!' onChange={searchValue}/>
            <button onClick={myFun}>Search</button>
        </div>
        <h4 className='msg'>{msg}</h4>
        <div className='categoriesSection'><h2 className='categoriesHeading'>Categories</h2>
        <div className='categoriesList'>
          {categories.map((category) =>(
          <div key={category.idCategory} className='categoryCard' onClick={()=>handleCategoryClick(category.strCategory)}>
            <img src={category.strCategoryThumb} alt={category.strCategory}/>
            <h4>{category.strCategory}</h4>
          </div>
        ))}
        </div>

        </div>
        <div>
          <MealCard detail ={data}/>
        </div>
    </div>
    </>
    
  )
}

export default Mainpage