import logo from './logo.svg';
import './App.css';
import Mainpage from './Component/Mainpage/Mainpage';
import { Route, Routes } from 'react-router';

function App() {
  return (
    
    <Routes>
      <Route path='/' element = { <Mainpage/>}/>
    </Routes>
  );
}

export default App;
